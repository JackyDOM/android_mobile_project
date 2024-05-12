package edu.rupp.firstite.modals;

public class Book {
    private int id;
    private int quantity;
    private BookData book;

    public static class BookData {
        private String book_image;
        private String title;
        private String price;

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

        public String getBook_image() {
            return book_image;
        }

        public void setBook_image(String book_image) {
            this.book_image = book_image;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BookData getBook() {
        return book;
    }

    public void setBook(BookData book) {
        this.book = book;
    }
}
