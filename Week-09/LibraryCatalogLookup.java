public class LibraryCatalog {
    public static String findBook(String[][] catalog, String targetIsbn) {
        int left = 0;
        int right = catalog.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String isbn = catalog[mid][0];
            String title = catalog[mid][1];
            
            int cmp = isbn.compareTo(targetIsbn);
            if (cmp == 0) {
                return title;
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return "Not Found";
    }
}
