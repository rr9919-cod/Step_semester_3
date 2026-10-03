# Problem 3 & 4: Pair With Target Sum (Unsorted)
def hasPairWithSum(nums, target):
    """
    Determines if there are any two distinct elements that add up to target using a hash set.
    Time Complexity: O(n)
    Space Complexity: O(n)
    """
    seen = set()
    for num in nums:
        complement = target - num
        if complement in seen:
            return True
        seen.add(num)
    return False

if __name__ == "__main__":
    print(hasPairWithSum([2, 7, 11, 15], 9))  # Expected: True
    print(hasPairWithSum([3, 4, 6], 20))        # Expected: False
