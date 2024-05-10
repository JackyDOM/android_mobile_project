package edu.rupp.firstite.modals;

public class Author {
    private int id;
    private String author_name;
    private String author_image;

    private String author_decs;

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

    private String gender;

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

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }
}
