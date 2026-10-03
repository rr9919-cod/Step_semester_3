import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanteenPopularOrder {
    public static class PopularResult {
        public String item;
        public int count;
        
        public PopularResult(String item, int count) {
            this.item = item;
            this.count = count;
        }
    }

    public static PopularResult mostPopular(List<String> orders) {
        Map<String, Integer> counts = new HashMap<>();
        for (String order : orders) {
            counts.put(order, counts.getOrDefault(order, 0) + 1);
        }
        
        String bestItem = "";
        int maxCount = -1;
        
        for (String order : orders) {
            int count = counts.get(order);
            if (count > maxCount) {
                maxCount = count;
                bestItem = order;
            }
        }
        
        return new PopularResult(bestItem, maxCount);
    }
}
