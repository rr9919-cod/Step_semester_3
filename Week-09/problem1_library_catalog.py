# Problem 1: Library Catalog Lookup
def findBook(catalog, targetIsbn):
    """
    Finds a book's title given its ISBN using binary search.
    Time Complexity: O(log n) where n is the number of books in the catalog.
    Space Complexity: O(1) auxiliary space.
    """
    left, right = 0, len(catalog) - 1
    while left <= right:
        mid = (left + right) // 2
        isbn, title = catalog[mid]
        if isbn == targetIsbn:
            return title
        elif isbn < targetIsbn:
            left = mid + 1
        else:
            right = mid - 1
    return "Not Found"

if __name__ == "__main__":
    catalog = [
        ("0001112223", "Introduction to Algebra"),
        ("0002223334", "Beginning Python"),
        ("0003334445", "Classic Mythology"),
        ("0004445556", "Data and Society"),
        ("0005556667", "European History")
    ]
    print(findBook(catalog, "0003334445"))  # Expected: "Classic Mythology"
    print(findBook(catalog, "0009998887"))  # Expected: "Not Found"
