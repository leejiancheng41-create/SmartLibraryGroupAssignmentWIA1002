// Represents one student library activity record stored in borrow_history.csv.
// The BorrowStack stores this record so history can be displayed in LIFO order.

public class HistoryRecord {
    private String matricNumber;
    private long isbn;
    private String title;
    private String author;
    private String action;
    private String dateTime;

    public HistoryRecord(String matricNumber, long isbn, String title, String author, String action, String dateTime) {
        this.matricNumber = matricNumber;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.action = action;
        this.dateTime = dateTime;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public long getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAction() {
        return action;
    }

    public String getDateTime() {
        return dateTime;
    }
}
