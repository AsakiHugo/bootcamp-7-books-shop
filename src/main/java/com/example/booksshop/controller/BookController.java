package com.example.booksshop.controller;

import com.example.booksshop.dao.BookBoughtDao;
import com.example.booksshop.entity.BookId;
import com.example.booksshop.entity.BooksBought;
import com.example.booksshop.service.BookService;
import com.example.booksshop.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;
    private final CartService cartService;
    private final BookBoughtDao bookBoughtDao;

    @GetMapping("/own-library")
    public String listOwnLibrary(Model model, Principal principal) {
        String userName = principal.getName();
        BooksBought booksBought = bookBoughtDao.findBooksBoughtByCustomerName(userName)
                .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("bookBought", booksBought);
        return "owner_library";
    }

    @GetMapping("/list-books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.listBooks());
        model.addAttribute("cartSize", cartService.cartSize());
        return "listbooks";
    }

    @ModelAttribute("cartSize")
    public Integer cartSize() {
        return cartService.cartSize();
    }

    @GetMapping("/book-details")
    public String bookDetails(@RequestParam("id") Integer id,
                              @RequestParam("isbn") String isbn,
                              Model model) {
        BookId bookId = null;

        if (Objects.nonNull(id) && Objects.nonNull(isbn)) {
            bookId = new BookId();
            bookId.setId(id);
            bookId.setIsbn(isbn);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        model.addAttribute("book", bookService.findBookById(bookId));

        return "bookdetails";
    }
}
