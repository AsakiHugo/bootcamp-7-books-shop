package com.example.booksshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String publisherName;
    private String email;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public Publisher(String publisherName, String email) {
        this.publisherName = publisherName;
        this.email = email;
    }

    public void addBook(Book book) {
        book.setPublisher(this);
        books.add(book);
    }
}
