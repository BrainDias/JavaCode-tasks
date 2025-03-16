import java.util.*;
import java.util.stream.Collectors;

class Order {
    private final String product;
    private final double cost;

    public Order(String product, double cost) {
        this.product = product;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public double getCost() {
        return cost;
    }
}

public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        // 2,3. Группируем заказы по продуктам и суммируем их стоимость
        Map<String, Double> productTotalCost = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getProduct,
                        Collectors.summingDouble(Order::getCost)
                ));

        // 4. Сортируем продукты по убыванию общей стоимости
        List<Map.Entry<String, Double>> sortedProducts = productTotalCost.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Сортируем по стоимости (по убыванию)
                .collect(Collectors.toList());

        // 5. Берем топ-3 самых дорогих продукта
        List<Map.Entry<String, Double>> top3Products = sortedProducts.stream()
                .limit(3)
                .collect(Collectors.toList());

        // 6. Выводим результат
        System.out.println("Топ-3 самых дорогих продуктов:");
        top3Products.forEach(entry -> 
            System.out.println(entry.getKey() + " - $" + entry.getValue())
        );

        // Общая стоимость топ-3 продуктов
        double totalTop3Cost = top3Products.stream()
                .mapToDouble(Map.Entry::getValue)
                .sum();
        
        System.out.println("Общая стоимость топ-3 продуктов: $" + totalTop3Cost);
    }
}
