package com.example.booksshop.controller;

import com.example.booksshop.dto.CartItem;
import com.example.booksshop.entity.Book;
import com.example.booksshop.entity.BookId;
import com.example.booksshop.service.BookService;
import com.example.booksshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final BookService bookService;

    @GetMapping("/clear-cart")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart/view-cart";
    }

    @GetMapping("/view-cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartItem", new CartItem());
        return "viewCart";
    }

    @PostMapping("/checkout")
    public String checkout(CartItem cartItem, Principal principal) {
        int i = 0;

        for (CartItem item : cartService.getCartItems()) {
            if (cartItem.getCartItemQuantity().get(i) == null) {
                item.setQuantity(1);
            } else {
                item.setQuantity(cartItem.getCartItemQuantity().get(i));
            }

            i++;
        }

        if (principal == null) {
            return "redirect:/auth/register";
        } else {
            return "redirect:/auth/info";
        }
    }

    @GetMapping("/delete")
    public String deleteCartItem(@RequestParam("id") int id,
                                 @RequestParam("isbn") String isbn) {
        cartService.deleteCartItem(id, isbn);
        return "redirect:/cart/view-cart";
    }

    @GetMapping("/add-cart")
    public String addToCart(@RequestParam("id") int id,
                            @RequestParam("isbn") String isbn,
                            @RequestParam("page") String page) {
        BookId bookId = new BookId();
        bookId.setId(id);
        bookId.setIsbn(isbn);

        Book book = bookService.findBookById(bookId);
        cartService.addToCart(book);

        if (page.equals("bookList")) {
            return "redirect:/book/list-books";
        } else {
            return "redirect:/book/book-details?id=" + id + "&isbn=" + isbn;
        }
    }
}
