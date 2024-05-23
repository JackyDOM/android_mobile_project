package edu.rupp.firstite.modals;

public class YourBookModal {
    private int id;
    private String card_number;
    private String card_holder_name;
    private String expeiration_date;
    private String cvv;

    private int price;

    private book book;

    private Category category;
    private Author author;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public YourBookModal.book getBook() {
        return book;
    }

    public void setBook(YourBookModal.book book) {
        this.book = book;
    }

    public static class book{
        private int id; // This is the actual book ID
        private String book_image;
        private String title;
        private String price;

        private String publisher;
        private String description;

        private String book_pdf;

        public String getBook_pdf() {
            return book_pdf;
        }

        public void setBook_pdf(String book_pdf) {
            this.book_pdf = book_pdf;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_holder_name() {
        return card_holder_name;
    }

    public void setCard_holder_name(String card_holder_name) {
        this.card_holder_name = card_holder_name;
    }

    public String getExpeiration_date() {
        return expeiration_date;
    }

    public void setExpeiration_date(String expeiration_date) {
        this.expeiration_date = expeiration_date;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
