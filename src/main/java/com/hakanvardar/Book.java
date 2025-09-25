package com.hakanvardar;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int year;
    private boolean available;

    public Book(String title, String author, String isbn, int year) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.available = true;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) - %s [ISBN: %s] %s", title, year, author, isbn,
                available ? "(Available)" : "(Borrowed)");
    }
}
