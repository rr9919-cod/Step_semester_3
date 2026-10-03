public class ClassTopperFinder {
    public static int[] findTopper(int[][] marks) {
        if (marks == null || marks.length == 0) {
            return new int[]{-1, 0};
        }
        
        int bestRow = 0;
        int maxTotal = -1;
        
        for (int r = 0; r < marks.length; r++) {
            int currentTotal = 0;
            for (int c = 0; c < marks[r].length; c++) {
                currentTotal += marks[r][c];
            }
            
            if (currentTotal > maxTotal) {
                maxTotal = currentTotal;
                bestRow = r;
            }
        }
        
        return new int[]{bestRow, maxTotal};
    }
}
