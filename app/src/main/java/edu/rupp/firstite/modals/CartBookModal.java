package edu.rupp.firstite.modals;

import edu.rupp.firstite.modals.Author;
import edu.rupp.firstite.modals.Category;

public class CartBookModal{
    private Book book;
    private int id;
    private Book title;
    private Book description;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    private String price;

    public Book getTitle() {
        return title;
    }

    public void setTitle(Book title) {
        this.title = title;
    }

    public Book getDescription() {
        return description;
    }

    public void setDescription(Book description) {
        this.description = description;
    }

    private String publisher;
    private Category category;
    private Author author;
    private String book_image;
    private String book_pdf;

    // Getters and setters
    // For id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // For price
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    // For publisher
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    // For category
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // For author
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    // For book_image
    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    // For book_pdf
    public String getBook_pdf() {
        return book_pdf;
    }

    public void setBook_pdf(String book_pdf) {
        this.book_pdf = book_pdf;
    }

}
