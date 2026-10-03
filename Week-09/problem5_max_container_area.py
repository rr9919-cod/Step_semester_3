# Problem 5: Maximize Area Between Two Boundaries
def maxContainerArea(heights):
    """
    Finds the maximum container water area using the two-pointer approach.
    Time Complexity: O(n)
    Space Complexity: O(1)
    """
    left = 0
    right = len(heights) - 1
    max_area = 0
    
    while left < right:
        width = right - left
        current_height = min(heights[left], heights[right])
        current_area = width * current_height
        max_area = max(max_area, current_area)
        
        if heights[left] < heights[right]:
            left += 1
        else:
            right -= 1
            
    return max_area

if __name__ == "__main__":
    heights = [1, 8, 6, 2, 5, 4, 8, 3, 7]
    print(maxContainerArea(heights))  # Expected: 49
