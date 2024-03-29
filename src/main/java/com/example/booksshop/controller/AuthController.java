package com.example.booksshop.controller;

import com.example.booksshop.dao.CustomerDao;
import com.example.booksshop.entity.Customer;
import com.example.booksshop.entity.Order;
import com.example.booksshop.entity.PaymentMethod;
import com.example.booksshop.service.AuthService;
import com.example.booksshop.service.CartService;
import com.example.booksshop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final CartService cartService;
    private final CustomerService customerService;
    private final CustomerDao customerDao;

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @PostMapping("/save-customer")
    public String saveCustomer(@RequestParam("billingAddress") String billingAddress,
                               @RequestParam("shippingAddress") String shippingAddress,
                               @RequestParam("payment") PaymentMethod method,
                               @ModelAttribute("totalPrice") double totalPrice,
                               Customer customer, BindingResult result) {
        Order order = new Order(
                LocalDate.now(),
                billingAddress,
                shippingAddress,
                method,
                totalPrice
        );

        if (result.hasErrors()) {
            return "register";
        }

        Optional<Customer> customer1 = customerDao.findCustomerByCustomerName(customer.getCustomerName());

        if (!customer1.isPresent()) {
            authService.register(customer, order);
        }

        this.customer = customer;

        return "redirect:/auth/info";
    }

    private Customer customer;

    @GetMapping("/info")
    public ModelAndView checkoutInfo(ModelMap map,
                                     @ModelAttribute("totalPrice") double totalPrice,
                                     Principal principal) {
        Optional<Customer> customer1 = null;

        if (principal == null) {
            customer1 = customerDao
                    .findCustomerByCustomerName(customer.getCustomerName());
        } else {
            customer1 = customerDao
                    .findCustomerByCustomerName(principal.getName());
        }

        if (customer1.isPresent()) {
            customerService.saveCustomerOrderItems(customer1.get());
        }

        ModelAndView mv = new ModelAndView();

        mv.addObject("cartItems", cartService.getCartItems());
        mv.addObject("totalPrice", totalPrice);

        if (principal != null) {
            mv.addObject("customerInfo", authService
                    .findCustomerInfoByCustomerName(principal.getName()));
        } else {
            mv.addObject("customerInfo", authService
                    .findCustomerInfoByCustomerName(customer.getCustomerName()));
        }

        mv.setViewName("info");

        return mv;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "info", required = false) String info) {
        if (info != null && info.equals("info")) {
            cartService.clearCart();
        }

        return "login";
    }

    @ModelAttribute("totalPrice")
    public double totalAmount() {
        Optional<Double> optionalDouble = cartService
                .getCartItems()
                .stream()
                .map(c -> c.getQuantity() * c.getPrice())
                .reduce((a, b) -> a + b);

        return optionalDouble.orElse(0.0);
    }
}
