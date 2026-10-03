import java.util.*;

public class WarehouseGrid {
    public static class SummaryResult {
        public int totalItems;
        public int[] maxCoordinate;
        
        public SummaryResult(int totalItems, int[] maxCoordinate) {
            this.totalItems = totalItems;
            this.maxCoordinate = maxCoordinate;
        }
    }

    public static SummaryResult warehouseSummary(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return new SummaryResult(0, new int[]{0, 0});
        }
        
        int totalItems = 0;
        int maxItems = -1;
        int[] maxCoordinate = new int[]{0, 0};
        
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                int items = grid[r][c];
                totalItems += items;
                
                if (items > maxItems) {
                    maxItems = items;
                    maxCoordinate = new int[]{r, c};
                }
            }
        }
        
        return new SummaryResult(totalItems, maxCoordinate);
    }
}
