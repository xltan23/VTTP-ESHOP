package vttp2022.paf.assessment.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.models.OrderStatusCount;
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;
import vttp2022.paf.assessment.eshop.services.OrderException;
import vttp2022.paf.assessment.eshop.services.OrderService;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

import static vttp2022.paf.assessment.eshop.Utils.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private OrderService orderSvc;

	@Autowired
	private WarehouseService warehouseSvc;
	
	// Payload consist of {name, lineItems[]}
	@PostMapping(path = {"/", ""}, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> postOrder(@RequestBody String payload) {
		// Payload should include {name:"", lineItems:[]}
		JsonObject jo = toJSON(payload);
		// Retrieve name from payload
		String name = jo.getString("name");

		// Check if Customer name is found in database
		Optional<Customer> optCustomer = customerRepo.findCustomerByName(name);
		if (optCustomer.isEmpty()) {
			JsonObject error = Json.createObjectBuilder()
									.add("Customer not found", name)
									.build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
									.body(error.toString());
		}

		// If Customer name found, proceed to insert order
		Order order = toOrder(jo);
		Customer customer = optCustomer.get();
		try {
			orderSvc.createOrder(order, customer);
		} catch (OrderException e) {
			JsonObject error = Json.createObjectBuilder()
									.add("Order unsuccessful", e.getMessage())
									.build();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
									.body(error.toString());
		}

		// If Order successful, proceed to dispatch order to warehouse
		OrderStatus orderStatus = warehouseSvc.dispatch(order);
		return ResponseEntity.ok(toOrderStatusJson(orderStatus).toString());
	}

	@GetMapping(path = "/{name}/status")
	public ResponseEntity<String> getOrderStatus(@PathVariable String name) {
		OrderStatusCount orderStatusCount = orderSvc.getOrderStatusCount(name);
		JsonObject jo = toOrderStatusCountJson(orderStatusCount);
		return ResponseEntity.ok(jo.toString());
	}
}
