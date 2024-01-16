package com.example.booksshop.entity;

import com.example.booksshop.dto.BookItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class BooksBought {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection
    private List<BookItem> books = new ArrayList<>();

    private String customerName;
    private LocalDate orderDate;
    private int orderId;

    public void addBook(BookItem book) {
        books.add(book);
    }
}
