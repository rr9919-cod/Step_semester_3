import java.util.List;

public class TicketPriceFinder {
    public static int findSlot(List<Integer> prices, int newPrice) {
        int left = 0;
        int right = prices.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int val = prices.get(mid);
            
            if (val == newPrice) {
                return mid;
            } else if (val < newPrice) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return left;
    }
}
