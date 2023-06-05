package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rikkei.academy.dto.CustomerDTO;
import rikkei.academy.model.Customer;
import rikkei.academy.service.ICustomerService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = {"/", "/customer"})
public class CustomerController {
    @Autowired
    private ICustomerService customerService;
    //Cấu hình upload file
    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping("/")
    public String showListCustomers(Model model) {
        List<Customer> customerList = customerService.findAll();
        model.addAttribute("customer", customerList);
        return "customer/list";
    }

    @GetMapping("/{id}")
    public String detailCustomer(@PathVariable Long id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "customer/detail";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        CustomerDTO customer = new CustomerDTO();
        model.addAttribute("customer", customer);
        return "customer/create";
    }

    @PostMapping("/create")
    public String actionCreate(CustomerDTO customerDTO, RedirectAttributes rs) {
        MultipartFile multipartFile = customerDTO.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Customer customer = new Customer(customerDTO.getId(), customerDTO.getName(), fileName);
        customerService.save(customer);
        rs.addFlashAttribute("validate", "them moi ok");
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id, Model model) {
        CustomerDTO customerDTO = new CustomerDTO();
        Customer customer = customerService.findById(id);
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        model.addAttribute("customer", customerDTO);
        model.addAttribute("avatar", customer.getAvatar());
        return "customer/edit";
    }

    @PostMapping("/update")
    public String actionUpdate(CustomerDTO customerDTO, RedirectAttributes rs) {
        MultipartFile multipartFile = customerDTO.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Customer customer = customerService.findById(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setAvatar(fileName);
        customerService.save(customer);
        rs.addFlashAttribute("validate", "sua ok");
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String showFormDelete(@PathVariable Long id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "customer/delete";
    }

    @PostMapping("/delete")
    public String actionDelete(Customer customer, RedirectAttributes rs) {
        customerService.deleteById(customer.getId());
        rs.addFlashAttribute("validate", "xoa ok");
        return "redirect:/";
    }
}
