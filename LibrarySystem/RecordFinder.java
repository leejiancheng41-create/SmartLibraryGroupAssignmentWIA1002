package LibrarySystem;// ============================================================
// Record Finder: Recursive ISBN Search & Duplicate Checker
// This class wraps the LibrarySystem.BookBST to provide detailed search output
// and duplicate detection logic.
// ============================================================

public class RecordFinder {

    private BookBST catalogue;

    // Constructor takes the shared LibrarySystem.BookBST catalogue
    public RecordFinder(BookBST catalogue) {
        this.catalogue = catalogue;
    }

    // FEATURE 1: Search by ISBN with full details printed
    // Uses the recursive search inside LibrarySystem.BookBST
    // Time complexity: O(log n) for a balanced BST
    public void searchByIsbn(long isbn) {
        System.out.println("\n[SEARCH] Looking for ISBN: " + isbn + "...");
        Book result = catalogue.search(isbn);

        if (result != null) {
            System.out.println("  LibrarySystem.Book Found!");
            System.out.println("  ISBN   : " + result.getIsbn());
            System.out.println("  Title  : " + result.getTitle());
            System.out.println("  Author : " + result.getAuthor());
            System.out.println("  Status : " + result.getStatus());
        } else {
            System.out.println("  No book found with ISBN: " + isbn);
        }
    }

    // Duplicate Checker
    public boolean isDuplicate(long isbn) {
        Book result = catalogue.search(isbn);
        if (result != null) {
            System.out.println("[DUPLICATE CHECK] ISBN " + isbn
                    + " already exists: \"" + result.getTitle() + "\" by " + result.getAuthor());
            return true;
        }
        return false;
    }

    // FEATURE 3: Safe Add - combines duplicate check + insert
    public void safeAdd(long isbn, String title, String author) {
        System.out.println("\n[ADD BOOK] Checking for duplicates...");
        if (isDuplicate(isbn)) {
            System.out.println("  Aborted: Cannot add duplicate ISBN.");
        } else {
            catalogue.insert(isbn, title, author);
        }
    }

    // FEATURE 4: Search by Title keyword (linear scan via in-order)
    public void searchByTitle(String keyword) {
        System.out.println("\n[TITLE SEARCH] Searching for: \"" + keyword + "\"...");
        boolean[] found = {false};
        searchByTitleHelper(catalogue.getRoot(), keyword.toLowerCase(), found);
        if (!found[0]) {
            System.out.println("  No books found matching: \"" + keyword + "\"");
        }
    }

    // Recursive in-order traversal to find matching titles
    private void searchByTitleHelper(Book node, String keyword, boolean[] found) {
        if (node == null) return;

        searchByTitleHelper(node.left, keyword, found);

        if (node.getTitle().toLowerCase().contains(keyword)) {
            System.out.println("  Match -> ISBN: " + node.getIsbn()
                    + " | \"" + node.getTitle() + "\" by " + node.getAuthor()
                    + " | " + node.getStatus());
            found[0] = true;
        }

        searchByTitleHelper(node.right, keyword, found);
    }
}
