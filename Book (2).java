 // a class called Book to represent each book with its title, author, isbn  

public class Book {

    private long isbn;
    private String title;
    private String author;
     Book left;
     Book right;

    public Book(long isbn, String title, String author) {

        this.isbn = isbn;
        this.title = title;
        this.author = author;
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
     //setters
    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

}
    

