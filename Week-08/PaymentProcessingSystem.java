import java.util.*;

// ================ Question 5: Payment Processing for a Shopping System ================

class Customer3 {
    private final String name;

    public Customer3(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

class Product {
    private final String name;
    private final double unitPrice;

    public Product(String name, double unitPrice) {
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
}

/** A line item: a product and the quantity ordered. */
class OrderItem {
    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getSubtotal() { return product.getUnitPrice() * quantity; }
}

/**
 * Interface for payment methods. Order/payment initiation logic depends
 * only on this abstraction, so new methods (e.g., UpiPayment) can be
 * added without modifying existing order-processing logic.
 */
interface PaymentMethod {
    String getMethodName();
    /** Attempts to process the given amount; returns true on success. */
    boolean processPayment(double amount);
}

class CreditCardPayment implements PaymentMethod {
    private final boolean simulateSuccess;

    public CreditCardPayment(boolean simulateSuccess) { this.simulateSuccess = simulateSuccess; }
    public String getMethodName() { return "Credit Card"; }
    public boolean processPayment(double amount) { return simulateSuccess; }
}

class PayPalPayment implements PaymentMethod {
    private final boolean simulateSuccess;

    public PayPalPayment(boolean simulateSuccess) { this.simulateSuccess = simulateSuccess; }
    public String getMethodName() { return "PayPal"; }
    public boolean processPayment(double amount) { return simulateSuccess; }
}

class BankTransferPayment implements PaymentMethod {
    private final boolean simulateSuccess;

    public BankTransferPayment(boolean simulateSuccess) { this.simulateSuccess = simulateSuccess; }
    public String getMethodName() { return "Bank Transfer"; }
    public boolean processPayment(double amount) { return simulateSuccess; }
}

enum OrderStatus {
    PENDING, PAID
}

/**
 * An order placed by a customer, containing items and tracking its own status.
 * Status can only be changed via payment outcomes, never set directly from outside.
 */
class Order {
    private static int counter = 0;

    private final String orderId;
    private final Customer3 customer;
    private final List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;

    public Order(Customer3 customer) {
        this.orderId = "Order-" + (++counter);
        this.customer = customer;
        this.status = OrderStatus.PENDING;
    }

    public String getOrderId() { return orderId; }
    public Customer3 getCustomer() { return customer; }
    public OrderStatus getStatus() { return status; }

    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    public boolean isEmpty() { return items.isEmpty(); }

    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) total += item.getSubtotal();
        return total;
    }

    /** Marks the order Paid only after a successful payment; failure leaves status unchanged. */
    void markPaid() { this.status = OrderStatus.PAID; }
}

/**
 * Facade coordinating orders and payments, printing required output.
 */
class PaymentProcessor {
    public Order createOrder(Customer3 customer) {
        Order order = new Order(customer);
        System.out.println("Order created for " + customer.getName() + ".");
        return order;
    }

    public void pay(Order order, PaymentMethod method) {
        if (order.isEmpty()) {
            System.out.println("Cannot process payment for an empty order.");
            return;
        }

        System.out.println("Payment initiated via " + method.getMethodName() + " for " + order.getOrderId() + ".");
        boolean success = method.processPayment(order.getTotal());

        if (success) {
            order.markPaid();
            System.out.println("Payment for " + order.getOrderId() + " successful. Order status: Paid.");
        } else {
            System.out.println("Payment for " + order.getOrderId() + " failed. Order status: Pending.");
        }
    }
}

public class PaymentProcessingSystem {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();

        Customer3 customerX = new Customer3("Customer X");
        Customer3 customerY = new Customer3("Customer Y");
        Customer3 customerZ = new Customer3("Customer Z");

        Product productA = new Product("Product A", 25.0);
        Product productB = new Product("Product B", 15.0);
        Product productC = new Product("Product C", 50.0);

        // Customer X creates an order with Product A (qty 2) and Product B (qty 1).
        Order orderX = processor.createOrder(customerX);
        orderX.addItem(productA, 2);
        orderX.addItem(productB, 1);

        // Customer X attempts to pay using Credit Card (succeeds).
        processor.pay(orderX, new CreditCardPayment(true));

        // Customer Y creates an empty order.
        Order orderY = processor.createOrder(customerY);

        // Customer Y attempts to pay for the empty order.
        processor.pay(orderY, new CreditCardPayment(true));

        // Customer Z creates an order with Product C (qty 1).
        Order orderZ = processor.createOrder(customerZ);
        orderZ.addItem(productC, 1);

        // Customer Z attempts to pay using PayPal (fails).
        processor.pay(orderZ, new PayPalPayment(false));
    }
}
