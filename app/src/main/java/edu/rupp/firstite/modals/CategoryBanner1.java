package edu.rupp.firstite.modals;

import java.util.Locale;

public class CategoryBanner1 {
    // create a variable
    private int id;
    private String book_image;
    private String title;

    private String price;
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public void setPrice(int price) {
        this.price = String.valueOf(price);
    }
}
