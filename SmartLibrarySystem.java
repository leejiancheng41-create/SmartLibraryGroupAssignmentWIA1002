import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmartLibrarySystem implements LibraryADT {
    private static final String BOOKS_FILE = "books.csv";
    private static final String STUDENTS_FILE = "students.csv";
    private static final String LIBRARIANS_FILE = "librarians.csv";
    private static final String HISTORY_FILE = "borrow_history.csv";

    private BookBST catalogue = new BookBST();
    private Map<String, String> students = new LinkedHashMap<>();
    private Map<String, String> librarians = new LinkedHashMap<>();

    public SmartLibrarySystem() {
        ensureCsvFilesExist();
        loadLibrarians();
        loadStudents();
        loadBooks();
    }

    @Override
    public boolean loginLibrarian(String username, String password) {
        return librarians.containsKey(username) && librarians.get(username).equals(password);
    }

    @Override
    public boolean studentExists(String matricNumber) {
        return students.containsKey(matricNumber);
    }

    @Override
    public boolean registerStudent(String matricNumber, String name) {
        if (matricNumber == null || matricNumber.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            return false;
        }
        if (students.containsKey(matricNumber)) {
            return false;
        }
        students.put(matricNumber, name);
        appendLine(STUDENTS_FILE, csv(matricNumber) + "," + csv(name));
        return true;
    }

    @Override
    public String getStudentName(String matricNumber) {
        return students.get(matricNumber);
    }

    @Override
    public boolean addBook(long isbn, String title, String author) {
        // Prevent duplicate entries gracefully.
        if (catalogue.search(isbn) != null) {
            return false;
        }
        catalogue.insert(new Book(isbn, title, author, false, ""));
        saveBooks();
        return true;
    }

    @Override
    public Book searchBook(long isbn) {
        return catalogue.search(isbn); // O(log n) recursive search by ISBN
    }

    @Override
    public boolean borrowBook(long isbn, String matricNumber) {
        if (!students.containsKey(matricNumber)) {
            return false;
        }

        Book book = catalogue.search(isbn);
        if (book == null || book.isBorrowed()) {
            return false;
        }

        book.markBorrowed(matricNumber);
        String now = now();
        HistoryRecord record = new HistoryRecord(matricNumber, book.getIsbn(), book.getTitle(), book.getAuthor(), "BORROW", now);
        appendHistory(record);
        saveBooks();
        return true;
    }

    @Override
    public boolean returnBook(long isbn, String matricNumber) {
        Book book = catalogue.search(isbn);
        if (book == null || !book.isBorrowed()) {
            return false;
        }

        // A student can only return a book currently borrowed under their matric number.
        if (!book.getBorrowedBy().equals(matricNumber)) {
            return false;
        }

        String now = now();
        HistoryRecord record = new HistoryRecord(matricNumber, book.getIsbn(), book.getTitle(), book.getAuthor(), "RETURN", now);
        appendHistory(record);
        book.markReturned();
        saveBooks();
        return true;
    }

    @Override
    public String viewLatestHistory(String matricNumber) {
        BorrowStack stack = new BorrowStack();

        ArrayList<HistoryRecord> records = loadHistoryRecords();
        for (int i = 0; i < records.size(); i++) {
            HistoryRecord r = records.get(i);
            if (r.getMatricNumber().equals(matricNumber)) {
                stack.push(r);
            }
        }

        return stack.displayHistory();
    }

    @Override
    public String viewCurrentlyBorrowed(String matricNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %-30s %-20s %-15s%n", "ISBN", "Title", "Author", "Status"));
        sb.append("--------------------------------------------------------------------------------\n");

        boolean found = false;
        ArrayList<Book> books = catalogue.getAllBooks();
        for (Book b : books) {
            if (b.isBorrowed() && b.getBorrowedBy().equals(matricNumber)) {
                found = true;
                sb.append(String.format("%-15d %-30s %-20s %-15s%n",
                        b.getIsbn(), b.getTitle(), b.getAuthor(), "BORROWED"));
            }
        }

        if (!found) {
            return "You are not currently borrowing any books.";
        }
        return sb.toString();
    }

    @Override
    public String viewAllBooksStatus() {
        ArrayList<Book> books = catalogue.getAllBooks();
        if (books.isEmpty()) {
            return "No books found in the catalogue.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %-30s %-20s %-25s%n", "ISBN", "Title", "Author", "Status"));
        sb.append("----------------------------------------------------------------------------------------------\n");

        for (Book b : books) {
            sb.append(String.format("%-15d %-30s %-20s %-25s%n",
                    b.getIsbn(), b.getTitle(), b.getAuthor(), b.getStatus()));
        }
        return sb.toString();
    }

    private void ensureCsvFilesExist() {
        createFileIfMissing(LIBRARIANS_FILE, "username,password\nadmin,1234\n");
        createFileIfMissing(STUDENTS_FILE, "matricNumber,name\nS001,Sample Student\n");
        createFileIfMissing(BOOKS_FILE, "isbn,title,author,status,borrowedBy\n");
        createFileIfMissing(HISTORY_FILE, "matricNumber,isbn,title,author,action,dateTime\n");
    }

    private void createFileIfMissing(String fileName, String defaultContent) {
        File file = new File(fileName);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.print(defaultContent);
            } catch (IOException e) {
                System.out.println("Unable to create " + fileName + ": " + e.getMessage());
            }
        }
    }

    private void loadLibrarians() {
        librarians.clear();
        ArrayList<String[]> rows = readCsvRows(LIBRARIANS_FILE);
        for (String[] row : rows) {
            if (row.length >= 2) {
                librarians.put(row[0], row[1]);
            }
        }
    }

    private void loadStudents() {
        students.clear();
        ArrayList<String[]> rows = readCsvRows(STUDENTS_FILE);
        for (String[] row : rows) {
            if (row.length >= 2) {
                students.put(row[0], row[1]);
            }
        }
    }

    private void loadBooks() {
        catalogue = new BookBST();
        ArrayList<String[]> rows = readCsvRows(BOOKS_FILE);
        for (String[] row : rows) {
            if (row.length >= 5) {
                try {
                    long isbn = Long.parseLong(row[0]);
                    String title = row[1];
                    String author = row[2];
                    boolean borrowed = row[3].equalsIgnoreCase("BORROWED");
                    String borrowedBy = row[4];
                    catalogue.insert(new Book(isbn, title, author, borrowed, borrowedBy));
                } catch (NumberFormatException e) {
                    // ignore invalid CSV row
                }
            }
        }
    }

    private void saveBooks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            writer.println("isbn,title,author,status,borrowedBy");
            ArrayList<Book> books = catalogue.getAllBooks();
            for (Book b : books) {
                String status = b.isBorrowed() ? "BORROWED" : "AVAILABLE";
                writer.println(csv(String.valueOf(b.getIsbn())) + "," + csv(b.getTitle()) + "," + csv(b.getAuthor()) + "," + csv(status) + "," + csv(b.getBorrowedBy()));
            }
        } catch (IOException e) {
            System.out.println("Unable to save books.csv: " + e.getMessage());
        }
    }

    private void appendHistory(HistoryRecord r) {
        appendLine(HISTORY_FILE,
                csv(r.getMatricNumber()) + "," +
                csv(String.valueOf(r.getIsbn())) + "," +
                csv(r.getTitle()) + "," +
                csv(r.getAuthor()) + "," +
                csv(r.getAction()) + "," +
                csv(r.getDateTime()));
    }

    private ArrayList<HistoryRecord> loadHistoryRecords() {
        ArrayList<HistoryRecord> records = new ArrayList<>();
        ArrayList<String[]> rows = readCsvRows(HISTORY_FILE);
        for (String[] row : rows) {
            if (row.length >= 6) {
                try {
                    records.add(new HistoryRecord(row[0], Long.parseLong(row[1]), row[2], row[3], row[4], row[5]));
                } catch (NumberFormatException e) {
                    // ignore invalid CSV row
                }
            }
        }
        return records;
    }

    private void appendLine(String fileName, String line) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(line);
        } catch (IOException e) {
            System.out.println("Unable to write to " + fileName + ": " + e.getMessage());
        }
    }

    // Reads CSV rows but skips the first header line.
    private ArrayList<String[]> readCsvRows(String fileName) {
        ArrayList<String[]> rows = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return rows;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                if (line.trim().isEmpty()) continue;
                rows.add(parseCsvLine(line));
            }
        } catch (IOException e) {
            System.out.println("Unable to read " + fileName + ": " + e.getMessage());
        }
        return rows;
    }

    // Simple CSV parser supporting quoted values and commas inside quotes.
    private String[] parseCsvLine(String line) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        values.add(current.toString());
        return values.toArray(new String[0]);
    }

    private String csv(String value) {
        if (value == null) value = "";
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
