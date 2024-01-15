package com.example.booksshop.dao;

import com.example.booksshop.dto.CustomerOrder;
import com.example.booksshop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Optional<Customer> findCustomerByCustomerName(String customerName);

    @Query("""
            select new com.example.booksshop.dto.CustomerOrder(c.customerName, c.email, c.phoneNumber, o.orderDate, o.billingAddress, o.shippingAddress) 
            from Customer c
            join c.orders o 
            where c.customerName = ?1
            """)
    Optional<CustomerOrder> customerOrderInfo(String customerName);
}
