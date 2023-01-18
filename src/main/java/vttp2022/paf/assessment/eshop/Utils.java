package vttp2022.paf.assessment.eshop;

import java.io.StringReader;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.models.OrderStatusCount;

public class Utils {
    
    public static JsonObject toJSON(String jsonString) {
        StringReader sr = new StringReader(jsonString);
        JsonReader jr = Json.createReader(sr);
        return jr.readObject();
    }

    public static JsonObject toOrderJson(Order order, String createdBy) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        List<JsonObject> lineItems = order.getLineItems().stream()
                                            .map(v -> toLineItemJson(v))
                                            .toList();
        for (JsonObject lineItem : lineItems) {
            jab.add(lineItem);
        }
        JsonArray ja = jab.build();
        return Json.createObjectBuilder()
                    .add("orderId", order.getOrderId())
                    .add("name", order.getName())
                    .add("email", order.getEmail())
                    .add("address", order.getAddress())
                    .add("createdBy", createdBy)
                    .add("lineItems", ja)
                    .build();
    }

    public static JsonObject toLineItemJson(LineItem li) {
        return Json.createObjectBuilder()
                    .add("item", li.getItem())
                    .add("quantity", li.getQuantity())
                    .build();
    }

    public static JsonObject toOrderStatusJson(OrderStatus orderStatus) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("orderId", orderStatus.getOrderId());
        if (orderStatus.getStatus().equals("dispatched")) {
            job.add("deliveryId", orderStatus.getDeliveryId());
        }
        job.add("status", orderStatus.getStatus());
        return job.build();
    }

    public static JsonObject toOrderStatusCountJson(OrderStatusCount orderStatusCount) {
        return Json.createObjectBuilder()
                    .add("name", orderStatusCount.getName())
                    .add("dispatched", orderStatusCount.getDispatched())
                    .add("pending", orderStatusCount.getPending())
                    .build();
    }

    public static Customer toCustomer(SqlRowSet srs) {
        Customer customer = new Customer();
        customer.setName(srs.getString("name"));
        customer.setAddress(srs.getString("address"));
        customer.setEmail(srs.getString("email"));
        return customer;
    }

    public static Order toOrder(JsonObject jo) {
        Order order = new Order();
        order.setName(jo.getString("name"));
        List<LineItem> lineItems = jo.getJsonArray("lineItems")
                                        .stream()
                                        // Each item in JsonArray is a JsonValue, treat as JsonObject
                                        .map(v -> v.asJsonObject())
                                        .map(c -> toLineItem(c))
                                        .toList();
        order.setLineItems(lineItems);
        return order;
    }

    public static LineItem toLineItem(JsonObject jo) {
        LineItem lineItem = new LineItem();
        lineItem.setItem(jo.getString("item"));
        lineItem.setQuantity(jo.getInt("quantity"));
        return lineItem;
    }

    // Only used if successfully dispatch order
    public static OrderStatus toOrderStatus(JsonObject jo) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(jo.getString("orderId"));
        orderStatus.setDeliveryId(jo.getString("deliveryId"));
        orderStatus.setStatus(jo.getString("dispatched"));
        return orderStatus;
    }

    public static OrderStatusCount toOrderStatusCount(SqlRowSet srs) {
        OrderStatusCount orderStatusCount = new OrderStatusCount();
        while(srs.next()) {
            orderStatusCount.setName(srs.getString("name"));
            String status = srs.getString("status");
            if (status.equals("pending")) {
                orderStatusCount.setPending(srs.getInt("status_count"));
            } else if (status.equals("dispatched")) {
                orderStatusCount.setDispatched(srs.getInt("status_count"));
            }
        }
        return orderStatusCount;
    }
}
