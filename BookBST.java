// a BST class to store books
// Original base: insert + recursive search by ISBN.
// Added: remove + traversal helpers for CSV saving and status display.

import java.util.ArrayList;

public class BookBST {

    private Book root;

    public Book getRoot() {
        return this.root;
    } // the root node of the BST

    // private method to insert a book into the BST
    private Book insertBook(Book root, Book book) {
        if (root == null) {
            return book;
        } else if (book.getIsbn() < root.getIsbn()) {
            root.left = insertBook(root.left, book);
        } else if (book.getIsbn() > root.getIsbn()) {
            root.right = insertBook(root.right, book);
        } else {
            System.out.println("Error: Book with ISBN " + book.getIsbn() + " already exists in the catalogue.");
        }
        return root;
    }

    // public method to insert a book into the BST
    public void insert(long isbn, String title, String author) {
        this.root = insertBook(this.root, new Book(isbn, title, author));
    }

    // added overload for loading books from CSV with borrowed status
    public void insert(Book book) {
        this.root = insertBook(this.root, book);
    }

    // private recursive method to search for a book in the BST (for Record finder)
    private Book searchBook(Book node, long isbn) {
        // Base case: not found
        if (node == null) return null;

        // Base case: found it
        if (node.getIsbn() == isbn) return node;

        // Recursive case: go left or right
        if (isbn < node.getIsbn()) {
            return searchBook(node.left, isbn);
        } else {
            return searchBook(node.right, isbn);
        }
    }

    // public method to search for a book in the BST (for Record finder)
    public Book search(long isbn) {
        return searchBook(this.root, isbn);
    }

    // public method to remove a book from the BST if needed
    public Book remove(long isbn) {
        Book removedBook = search(isbn);
        if (removedBook != null) {
            this.root = removeBook(this.root, isbn);
        }
        return removedBook;
    }

    // private recursive method to remove a book from BST
    private Book removeBook(Book node, long isbn) {
        if (node == null) return null;

        if (isbn < node.getIsbn()) {
            node.left = removeBook(node.left, isbn);
        } else if (isbn > node.getIsbn()) {
            node.right = removeBook(node.right, isbn);
        } else {
            // Case 1: no child
            if (node.left == null && node.right == null) {
                return null;
            }

            // Case 2: one child
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            // Case 3: two children
            Book smallest = findMin(node.right);
            node.setIsbn(smallest.getIsbn());
            node.setTitle(smallest.getTitle());
            node.setAuthor(smallest.getAuthor());
            node.setBorrowed(smallest.isBorrowed());
            node.setBorrowedBy(smallest.getBorrowedBy());
            node.right = removeBook(node.right, smallest.getIsbn());
        }
        return node;
    }

    // find the smallest ISBN in a subtree
    private Book findMin(Book node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // return all books in sorted ISBN order for CSV saving / display
    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        inorder(root, books);
        return books;
    }

    private void inorder(Book node, ArrayList<Book> books) {
        if (node == null) return;
        inorder(node.left, books);
        books.add(node);
        inorder(node.right, books);
    }
}
