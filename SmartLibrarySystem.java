import java.util.Stack;

public class SmartLibrarySystem implements LibraryADT {
    private BookBST catalogue = new BookBST();
    private Stack<Book> borrowHistory = new Stack<>(); // Stack for LIFO history tracking

    @Override
    public boolean addBook(long isbn, String title, String author) {
        // Prevent duplicate entries gracefully
        if (catalogue.search(isbn) != null) {
            return false;
        }
        catalogue.insert(isbn, title, author);
        return true;
    }

    @Override
    public Book searchBook(long isbn) {
        return catalogue.search(isbn); // O(log n) recursive search
    }

    @Override
    public boolean borrowBook(long isbn) {
        Book book = catalogue.search(isbn);
        if (book != null) {
            borrowHistory.push(book); // Push record to the history stack
            return true;
        }
        return false;
    }

    @Override
    public String viewLatestHistory() {
        if (borrowHistory.isEmpty()) {
            return "No books have been borrowed yet.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %-30s %-20s\n", "ISBN", "Title", "Author"));
        sb.append("-------------------------------------------------------------\n");

        // Reverse iteration to show most recent activity first (LIFO)
        for (int i = borrowHistory.size() - 1; i >= 0; i--) {
            Book b = borrowHistory.get(i);
            sb.append(String.format("%-15d %-30s %-20s\n", b.getIsbn(), b.getTitle(), b.getAuthor()));
        }
        return sb.toString();
    }
}