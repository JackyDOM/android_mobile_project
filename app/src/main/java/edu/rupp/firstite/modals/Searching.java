package edu.rupp.firstite.modals;

public class Searching {
    private int id;
    private String title;
    private String description;
    private String price;
    private String publisher;
    private Category category;
    private Author author;
    private String book_image;
    private String book_pdf;

    // Getters and setters for the fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

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

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public String getBook_pdf() {
        return book_pdf;
    }

    public void setBook_pdf(String book_pdf) {
        this.book_pdf = book_pdf;
    }

    // Nested Category class
    public static class Category {
        private int id;
        private String name;

        // Getters and setters for the fields

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // Nested Author class
    public static class Author {
        private int id;
        private String author_name;
        private String author_decs;
        private String gender;
        private String author_image;

        // Getters and setters for the fields

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getAuthor_decs() {
            return author_decs;
        }

        public void setAuthor_decs(String author_decs) {
            this.author_decs = author_decs;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAuthor_image() {
            return author_image;
        }

        public void setAuthor_image(String author_image) {
            this.author_image = author_image;
        }
    }
}
