package com.example.booksshop.dto;

import com.example.booksshop.entity.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemInfo {
    private int orderItemId;
    private double totalPrice;
    private Book book;
    private LocalDate orderDate;

    public OrderItemInfo(int orderItemId, double totalPrice, Book book, LocalDate orderDate) {
        this.orderItemId = orderItemId;
        this.totalPrice = totalPrice;
        this.book = book;
        this.orderDate = orderDate;
    }
}
