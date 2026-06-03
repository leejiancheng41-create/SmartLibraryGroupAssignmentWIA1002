// ============================================================
// Record Finder: Recursive ISBN Search & Duplicate Checker
// This class wraps the BookBST to provide detailed search output
// and duplicate detection logic.
// ============================================================

public class RecordFinder {

    private BookBST catalogue;

    // Constructor takes the shared BookBST catalogue
    public RecordFinder(BookBST catalogue) {
        this.catalogue = catalogue;
    }

    // -------------------------------------------------------
    // FEATURE 1: Search by ISBN with full details printed
    // Uses the recursive search inside BookBST
    // Time complexity: O(log n) for a balanced BST
    // -------------------------------------------------------
    public void searchByIsbn(long isbn) {
        System.out.println("\n[SEARCH] Looking for ISBN: " + isbn + "...");
        Book result = catalogue.search(isbn);

        if (result != null) {
            System.out.println("  Book Found!");
            System.out.println("  ISBN   : " + result.getIsbn());
            System.out.println("  Title  : " + result.getTitle());
            System.out.println("  Author : " + result.getAuthor());
        } else {
            System.out.println("  No book found with ISBN: " + isbn);
        }
    }

    // -------------------------------------------------------
    // Duplicate Checker
    // Checks if an ISBN already exists before adding a book.
    // Returns true if duplicate found, false if safe to add.
    // -------------------------------------------------------
    public boolean isDuplicate(long isbn) {
        Book result = catalogue.search(isbn);
        if (result != null) {
            System.out.println("[DUPLICATE CHECK] ISBN " + isbn 
                + " already exists: \"" + result.getTitle() + "\" by " + result.getAuthor());
            return true;
        }
        return false;
    }

    // -------------------------------------------------------
    // FEATURE 3: Safe Add - combines duplicate check + insert
    // Call this instead of catalogue.insert() to be safe
    // -------------------------------------------------------
    public void safeAdd(long isbn, String title, String author) {
        System.out.println("\n[ADD BOOK] Checking for duplicates...");
        if (isDuplicate(isbn)) {
            System.out.println("  Aborted: Cannot add duplicate ISBN.");
        } else {
            catalogue.insert(isbn, title, author);
        }
    }

    // -------------------------------------------------------
    // FEATURE 4: Search by Title keyword (linear scan via in-order)
    // Bonus feature - searches across all books by title keyword
    // Time complexity: O(n) since it must visit all nodes
    // -------------------------------------------------------
    public void searchByTitle(String keyword) {
        System.out.println("\n[TITLE SEARCH] Searching for: \"" + keyword + "\"...");
        boolean[] found = {false}; // array trick to modify inside recursive method
        searchByTitleHelper(catalogue.getRoot(), keyword.toLowerCase(), found);
        if (!found[0]) {
            System.out.println("  No books found matching: \"" + keyword + "\"");
        }
    }

    // Recursive in-order traversal to find matching titles
    private void searchByTitleHelper(Book node, String keyword, boolean[] found) {
        if (node == null) return;

        // Visit left
        searchByTitleHelper(node.left, keyword, found);

        // Check current node
        if (node.getTitle().toLowerCase().contains(keyword)) {
            System.out.println("  Match -> ISBN: " + node.getIsbn()
                + " | \"" + node.getTitle() + "\" by " + node.getAuthor());
            found[0] = true;
        }

        // Visit right
        searchByTitleHelper(node.right, keyword, found);
    }
}
