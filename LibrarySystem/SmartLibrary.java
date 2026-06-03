package LibrarySystem;

import java.util.InputMismatchException;
import java.util.Scanner;

// Console interface for Librarian and Student users.
// Users can interact without modifying source code because data is saved to CSV files.
public class SmartLibrary {
    private LibraryADT library;
    private Scanner scanner;
    private String currentStudentMatric;

    public SmartLibrary() {
        library = new SmartLibrarySystem();
        scanner = new Scanner(System.in);
    }

    public void runMenu() {
        int choice;

        do {
            printRoleMenu();
            choice = readInt("Choose role: ");

            switch (choice) {
                case 1:
                    librarianLoginInterface();
                    break;
                case 2:
                    studentLoginInterface();
                    break;
                case 3:
                    System.out.println("Thank you for using Smart Library System.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }

            System.out.println();
        } while (choice != 3);
    }

    private void printRoleMenu() {
        System.out.println("====================================");
        System.out.println("        SMART LIBRARY SYSTEM        ");
        System.out.println("====================================");
        System.out.println("1. Librarian Login");
        System.out.println("2. Student Login");
        System.out.println("3. Exit");
        System.out.println("====================================");
    }

    private void librarianLoginInterface() {
        System.out.println("\n--- Librarian Login ---(For testing purposes, username: admin, password :1234)");
        String username = readNonEmptyString("Username: ");
        String password = readNonEmptyString("Password: ");

        if (library.loginLibrarian(username, password)) {
            System.out.println("Login successful. Welcome, " + username + ".");
            runLibrarianMenu();
        } else {
            System.out.println("Login failed. Wrong username or password.");
        }
    }

    private void studentLoginInterface() {
        System.out.println("\n--- Student Login ---");
        String matric = readNonEmptyString("Enter matric number: ");

        if (!library.studentExists(matric)) {
            System.out.println("Matric number not found.");
            String register = readNonEmptyString("Register this student now? (Y/N): ");
            if (register.equalsIgnoreCase("Y")) {
                String name = readNonEmptyString("Enter student name: ");
                if (library.registerStudent(matric, name)) {
                    System.out.println("Student registered successfully.");
                } else {
                    System.out.println("Student registration failed.");
                    return;
                }
            } else {
                System.out.println("Returning to role menu...");
                return;
            }
        }

        currentStudentMatric = matric;
        System.out.println("Login successful. Welcome, " + library.getStudentName(matric) + ".");
        runStudentMenu();
        currentStudentMatric = null;
    }

    private void runLibrarianMenu() {
        int choice;

        do {
            System.out.println("\n========== LIBRARIAN MENU ==========");
            System.out.println("1. Add Book");
            System.out.println("2. Search Book / Check Status");
            System.out.println("3. View All Book Status");
            System.out.println("4. Back to Role Menu");
            System.out.println("====================================");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addBookInterface();
                    break;
                case 2:
                    searchBookInterface();
                    break;
                case 3:
                    viewAllBooksStatusInterface();
                    break;
                case 4:
                    System.out.println("Returning to role menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 4.");
            }
        } while (choice != 4);
    }

    private void runStudentMenu() {
        int choice;

        do {
            System.out.println("\n=========== STUDENT MENU ===========");
            System.out.println("1. Search Book / Check Status");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. View My Borrowing History");
            System.out.println("5. View My Currently Borrowed Books");
            System.out.println("6. Back to Role Menu");
            System.out.println("====================================");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    searchBookInterface();
                    break;
                case 2:
                    borrowBookInterface();
                    break;
                case 3:
                    returnBookInterface();
                    break;
                case 4:
                    viewHistoryInterface();
                    break;
                case 5:
                    viewCurrentlyBorrowedInterface();
                    break;
                case 6:
                    System.out.println("Returning to role menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 6.");
            }
        } while (choice != 6);
    }

    private void addBookInterface() {
        System.out.println("\n--- Add Book ---");

        long isbn = readLong("Enter ISBN: ");
        scanner.nextLine(); // clear newline after number input

        String title = readNonEmptyString("Enter Title: ");
        String author = readNonEmptyString("Enter Author: ");

        boolean isAdded = library.addBook(isbn, title, author);

        if (isAdded) {
            System.out.println("Book added successfully and saved into books.csv.");
        } else {
            System.out.println("Error: A book with this ISBN already exists.");
        }
    }

    private void searchBookInterface() {
        System.out.println("\n--- Search Book / Check Status ---");

        long isbn = readLong("Enter ISBN to search: ");
        Book foundBook = library.searchBook(isbn);

        if (foundBook == null) {
            System.out.println("Book not found in catalogue.");
        } else {
            displayBook(foundBook);
        }
    }

    private void borrowBookInterface() {
        System.out.println("\n--- BorrowBook ---");

        long isbn = readLong("Enter ISBN to borrow: ");
        boolean isBorrowed = library.borrowBook(isbn, currentStudentMatric);

        if (isBorrowed) {
            System.out.println("Book borrowed successfully. books.csv and borrow_history.csv updated.");
        } else {
            System.out.println("Borrow failed. Book not found, already borrowed, or student login invalid.");
        }
    }

    private void returnBookInterface() {
        System.out.println("\n--- Return Book ---");

        long isbn = readLong("Enter ISBN to return: ");
        boolean isReturned = library.returnBook(isbn, currentStudentMatric);

        if (isReturned) {
            System.out.println("Book returned successfully. books.csv and borrow_history.csv updated.");
        } else {
            System.out.println("Return failed. This book is not borrowed by your matric number.");
        }
    }

    private void viewHistoryInterface() {
        System.out.println("\n--- My Borrowing History ---");
        System.out.println(library.viewLatestHistory(currentStudentMatric));
    }

    private void viewCurrentlyBorrowedInterface() {
        System.out.println("\n--- My Currently Borrowed Books ---");
        System.out.println(library.viewCurrentlyBorrowed(currentStudentMatric));
    }

    private void viewAllBooksStatusInterface() {
        System.out.println("\n--- All Book Status ---");
        System.out.println(library.viewAllBooksStatus());
    }

    private void displayBook(Book book) {
        System.out.println("------------------------------------");
        System.out.println("ISBN  : " + book.getIsbn());
        System.out.println("Title : " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Status: " + book.getStatus());
        System.out.println("------------------------------------");
    }

    private int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a whole number.");
                scanner.nextLine();
            }
        }
    }

    private long readLong(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Invalid ISBN. Please enter numbers only.");
                scanner.nextLine();
            }
        }
    }

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
}
