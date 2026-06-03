// a class called Book to represent each book with its title, author, isbn
// Added fields: borrowed status and borrowedBy matric number for CSV record tracking.

public class Book {

    private long isbn;
    private String title;
    private String author;
    private boolean borrowed;
    private String borrowedBy;

    Book left;
    Book right;

    public Book(long isbn, String title, String author) {
        this(isbn, title, author, false, "");
    }

    public Book(long isbn, String title, String author, boolean borrowed, String borrowedBy) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.borrowed = borrowed;
        this.borrowedBy = borrowedBy == null ? "" : borrowedBy;
        this.left = null;
        this.right = null;
    }

    // getters
    public long getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public String getStatus() {
        if (borrowed) {
            return "BORROWED by " + borrowedBy;
        }
        return "AVAILABLE";
    }

    // setters
    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy == null ? "" : borrowedBy;
    }

    public void markBorrowed(String matricNumber) {
        this.borrowed = true;
        this.borrowedBy = matricNumber;
    }

    public void markReturned() {
        this.borrowed = false;
        this.borrowedBy = "";
    }
}
