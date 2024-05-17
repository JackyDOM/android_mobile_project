package edu.rupp.firstite.modals;

public class CategoryBanner2{
    private int id;
    private String book_image;
    private String book_pdf;
    private String title;

    private String price;

    private String description;


    private Category category;

    private String publisher;

    private Author author;

    public String getDescription() {
        return description;
    }

    public String getBook_pdf() {
        return book_pdf;
    }

    public void setBook_pdf(String book_pdf) {
        this.book_pdf = book_pdf;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
