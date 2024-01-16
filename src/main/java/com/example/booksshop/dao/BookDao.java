package com.example.booksshop.dao;

import com.example.booksshop.dto.OrderItemInfo;
import com.example.booksshop.entity.Book;
import com.example.booksshop.entity.BookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookDao extends JpaRepository<Book, BookId> {
}
