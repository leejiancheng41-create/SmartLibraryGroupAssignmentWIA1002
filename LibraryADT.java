// ADT interface for the Smart Library System.
// Users interact through these methods without knowing the internal BST/Stack/CSV logic.

public interface LibraryADT {
    boolean loginLibrarian(String username, String password);

    boolean studentExists(String matricNumber);

    boolean registerStudent(String matricNumber, String name);

    String getStudentName(String matricNumber);

    boolean addBook(long isbn, String title, String author);

    Book searchBook(long isbn);

    boolean borrowBook(long isbn, String matricNumber);

    boolean returnBook(long isbn, String matricNumber);

    String viewLatestHistory(String matricNumber);

    String viewCurrentlyBorrowed(String matricNumber);

    String viewAllBooksStatus();
}
