package edu.rupp.firstite.modals;

import com.google.gson.annotations.SerializedName;

public class Book {
    private int id;
//    private String title;
//    private String description;
//    private String price;
//    private String publisher;
//    private Category category;
//    private Author author;

    //@SerializedName("book_image")
    private String book_Image;

    public void setId(int id) {
        this.id = id;
    }

    public void setBook_Image(String book_Image) {
        this.book_Image = book_Image;
    }

    public int getId() {
        return id;
    }

    public String getBook_Image() {
        return book_Image;
    }
}
