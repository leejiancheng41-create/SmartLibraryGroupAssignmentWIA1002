public interface LibraryADT {

    boolean addBook(long isbn, String title, String author);

    Book searchBook(long isbn);

    boolean borrowBook(long isbn);

    String viewLatestHistory();
}