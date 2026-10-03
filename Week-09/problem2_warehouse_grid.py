# Problem 2: Warehouse Grid Summary
def warehouseSummary(grid):
    """
    Calculates total items and finds coordinates of the maximum item count.
    Time Complexity: O(m * n) where m is rows and n is columns.
    Space Complexity: O(1) auxiliary space.
    """
    if not grid or not grid[0]:
        return (0, (-1, -1))
        
    total_items = 0
    max_val = -1
    max_coordinate = (0, 0)
    
    for r in range(len(grid)):
        for c in range(len(grid[r])):
            val = grid[r][c]
            total_items += val
            if val > max_val:
                max_val = val
                max_coordinate = (r, c)
                
    return (total_items, max_coordinate)

if __name__ == "__main__":
    grid = [
        [4, 9, 2],
        [7, 1, 6],
        [3, 12, 5]
    ]
    print(warehouseSummary(grid))  # Expected: (49, (2, 1))
