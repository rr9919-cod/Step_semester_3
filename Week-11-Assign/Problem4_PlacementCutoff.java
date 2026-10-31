// Time Complexity: Average O(n), Worst Case O(n^2)
// Extra Space: O(1) iterative or O(log n) recursive stack
// Stable: No
public class Problem4_PlacementCutoff {
    public static int kthHighest(int[] marks, int k) {
        int targetIndex = marks.length - k;
        return quickSelect(marks, 0, marks.length - 1, targetIndex);
    }
    
    private static int quickSelect(int[] arr, int low, int high, int target) {
        if (low <= high) {
            int pivotIndex = partition(arr, low, high);
            if (pivotIndex == target) {
                return arr[pivotIndex];
            } else if (pivotIndex < target) {
                return quickSelect(arr, pivotIndex + 1, high, target);
            } else {
                return quickSelect(arr, low, pivotIndex - 1, target);
            }
        }
        return -1;
    }
    
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}
