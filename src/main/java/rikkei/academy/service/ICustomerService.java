package rikkei.academy.service;

import rikkei.academy.model.Customer;

import java.util.List;

public interface ICustomerService {
    List<Customer> findAll();

    Customer findById(Long id);

    void save(Customer customer);
    void deleteById(Long id);
}
