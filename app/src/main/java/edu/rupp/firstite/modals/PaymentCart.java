package edu.rupp.firstite.modals;

public class PaymentCart {
    private int user_id;
    private int book_id;
    private String card_number;
    private String card_holder_name;
    private String expiration_date;
    private String cvv;
    private double price;

    // Constructor
    public PaymentCart(int user_id, int book_id, String card_number, String card_holder_name, String expiration_date, String cvv, double price) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.card_number = card_number;
        this.card_holder_name = card_holder_name;
        this.expiration_date = expiration_date;
        this.cvv = cvv;
        this.price = price;
    }

    // Getters and Setters
    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getBookId() {
        return book_id;
    }

    public void setBookId(int book_id) {
        this.book_id = book_id;
    }

    public String getCardNumber() {
        return card_number;
    }

    public void setCardNumber(String card_number) {
        this.card_number = card_number;
    }

    public String getCardHolderName() {
        return card_holder_name;
    }

    public void setCardHolderName(String card_holder_name) {
        this.card_holder_name = card_holder_name;
    }

    public String getExpirationDate() {
        return expiration_date;
    }

    public void setExpirationDate(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
