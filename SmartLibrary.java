import java.util.*;

public class SmartLibrary{

    private LibraryADT library;
    private Scanner scanner;

    public SmartLibrary() {
        // need interface implemented by other
        // TODO
        // this.library = new SmartLibrarySystem();
        this.scanner = new Scanner(System.in);
    }


    public void runMenu() {
        int choice;

        do {
            printMenu();
            choice = readInt("Enter your choice: ");

            switch (choice)
            {
                case 1 -> addBookInterface();
                case 2 -> searchBookInterface();
                case 3 -> borrowBookInterface();
                case 4 -> System.out.println("Thank you for using Smart Library System.");
                default -> System.out.println("Invalid choice. Please enter a number from 1 to 5.");
            }
            System.out.println();

        } while (choice != 5);
    }

    // print menu
    private void printMenu() {
        System.out.println("====================================");
        System.out.println("        SMART LIBRARY SYSTEM        ");
        System.out.println("====================================");
        System.out.println("1. Add Book");
        System.out.println("2. Search Book");
        System.out.println("3. Borrow Book");
        System.out.println("4. View Borrowing History");
        System.out.println("5. Exit");
        System.out.println("====================================");
    }

    //add book
    private void addBookInterface() {
        System.out.println("\n--- Add Book ---");

        long isbn = readLong("Enter ISBN: ");
        scanner.nextLine();

        String title = readNonEmptyString("Enter Title: ");
        String author = readNonEmptyString("Enter Author: ");

        // Check duplicate before adding
        boolean isAdded = library.addBook(isbn, title, author);

        if (isAdded) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Error: A book with this ISBN already exists.");
        }

    }

    //search book
    private void searchBookInterface() {
        System.out.println("\n--- Search Book ---");

        long isbn = readLong("Enter ISBN to search: ");
        Book foundBook = library.searchBook(isbn);

        if (foundBook == null) {
            System.out.println("Book not found.");
        } else {
            displayBook(foundBook);
        }
    }

    //borrow book
    private void borrowBookInterface() {
        System.out.println("\n--- Borrow Book ---");

        long isbn = readLong("Enter ISBN to borrow: ");
        boolean isBorrowed = library.borrowBook(isbn);

        if (isBorrowed) {
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Borrow failed. Book not found in catalogue.");
        }
    }


    //display book
    private void displayBook(Book book) {
        System.out.println("------------------------------------");
        System.out.println("ISBN  : " + book.getIsbn());
        System.out.println("Title : " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("------------------------------------");
    }

    //suggested safe input
    private int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a whole number.");
                scanner.nextLine(); // clear wrong input
            }
        }
    }

    //suggested safe input
    private long readLong(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Invalid ISBN. Please enter numbers only.");
                scanner.nextLine(); // clear wrong input
            }
        }
    }

    //suggested safe read
    private String readNonEmptyString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private void viewHistoryInterface()
    {
        System.out.println("\n--- Borrow History ---");
        System.out.println(library.viewLatestHistory());
    }
}
