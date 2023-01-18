package vttp2022.paf.assessment.eshop.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatusCount;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepo;

    @Transactional(rollbackFor = { OrderException.class })
    public void createOrder(Order order, Customer customer) throws OrderException {
        // Generate random 8 characters order ID  
        String orderId = UUID.randomUUID().toString().substring(0,8);
        order.setOrderId(orderId);
        order.setAddress(customer.getAddress());
        order.setEmail(customer.getEmail());
        try {
            orderRepo.insertOrder(order);
        } catch (Exception e) {
            throw new OrderException(e.getMessage(), e);
        }
    }

    public OrderStatusCount getOrderStatusCount(String name) {
        OrderStatusCount orderStatusCount = orderRepo.getOrdersCount(name);
        return orderStatusCount;
    }    
}
