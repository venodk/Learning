import java.util.*;

public class Personio {

    public static void main(String[] args) {
        List<Product> inventoryList = List.of(
                new Product(1, 5),
                new Product(2, 10),
                new Product(3, 6));
        Inventory inventory = new Inventory(inventoryList);

        Order orders1 = new Order(1, List.of(new Product(1, 3), new Product(2, 5)));
        Order orders2 = new Order(2, List.of(new Product(2, 6), new Product(3, 3)));
        Order orders3 = new Order(3, List.of(new Product(1, 2), new Product(2, 5), new Product(3, 6)));

        List<OrderStatus> status1 = inventory.processOrders(List.of(orders1));
        List<OrderStatus> status2 = inventory.processOrders(List.of(orders2));
        List<OrderStatus> status3 = inventory.processOrders(List.of(orders3));
        System.out.println(status1);
        System.out.println(status2);
        System.out.println(status3);

        inventory.updateInventory(4, 7);
        Order orders4 = new Order(4, List.of(new Product(4, 7)));
        List<OrderStatus> status4 = inventory.processOrders(List.of(orders4));
        System.out.println(status4);
    }

    public static class Inventory {
        private final Map<Long, Product> store;

        public Inventory(List<Product> products) {
            this.store = new HashMap<>();
            for (Product product : products) {
                Product inventoryProduct = this.store.computeIfAbsent(product.id, k -> new Product(product.id, 0));
                inventoryProduct.count += product.count;
            }
        }

        public List<OrderStatus> processOrders(List<Order> orders) {
            List<OrderStatus> result = new ArrayList<>();
            for (Order order : orders) {
                if (order.products.stream().allMatch(this::isProductAvailable)) {
                    order.products.forEach(product -> this.store.get(product.id).count -= product.count);
                    result.add(new OrderStatus(order.orderId, "SUCCESS"));
                } else {
                    result.add(new OrderStatus(order.orderId, "FAILED"));
                }
            }
            return result;
        }

        private boolean isProductAvailable(Product product) {
            return store.get(product.id) != null && store.get(product.id).count >= product.count;
        }

        public void updateInventory(long productId, long count) {
            this.store.put(productId, new Product(productId, count));
        }
    }

    public static class Product {
        private final long id;
        private long count;
        public Product(long id, long count) {
            this.count = count;
            this.id = id;
        }
    }

    public record Order(long orderId, List<Product> products) {}
    public record OrderStatus(long orderId, String status) {}
}
