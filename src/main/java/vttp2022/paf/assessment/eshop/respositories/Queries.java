package vttp2022.paf.assessment.eshop.respositories;

public class Queries {
    
    public static final String SQL_FIND_CUSTOMER_BY_NAME = "SELECT * FROM customer WHERE name = ?";

    public static final String SQL_INSERT_ORDER = "INSERT INTO purchase_order(order_id, order_date, name, address, email) VALUES (?, ?, ?, ?, ?)";

    public static final String SQL_INSERT_LINEITEM = "INSERT INTO line_item(item, quantity, order_id) VALUES (?, ?, ?)";

    public static final String SQL_INSERT_ORDER_STATUS = "INSERT INTO order_status(order_id, delivery_id, status) VALUES (?, ?, ?)";

    public static final String SQL_GET_ORDER_STATUS_BY_NAME = "SELECT * FROM order_status_count WHERE name = ?";
}
