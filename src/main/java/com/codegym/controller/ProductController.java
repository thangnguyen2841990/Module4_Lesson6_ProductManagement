package com.codegym.controller;

import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    IProductService productService;
    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping
    private ModelAndView showAllProduct(String name) {
        if (name == null){
            List<Product> products = productService.findAll();
            ModelAndView modelAndView = new ModelAndView("/product/list");
            modelAndView.addObject("products", products);
            return modelAndView;
        } else {
            String name1 = "%" + name + "%";
            List<Product> products = this.productService.findByName(name1);
            ModelAndView modelAndView = new ModelAndView("/product/list");
            modelAndView.addObject("products", products);
            return modelAndView;
        }


    }
    @GetMapping("/view/{id}")
    private ModelAndView showProductDetail(@PathVariable Long id) {
        Product product = this.productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product/view");
        modelAndView.addObject("product", product);
        return modelAndView;
    }
    @GetMapping("/create")
    private ModelAndView showFormProduct() {
        ProductForm productForm = new ProductForm();
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("productForm", productForm);
        return modelAndView;
    }

    @PostMapping("/create")
    private ModelAndView createProduct(@ModelAttribute ProductForm productForm) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        long currentTime = System.currentTimeMillis();
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productForm.getName(), productForm.getPrice(), productForm.getQuantity(), productForm.getDescription(), fileName);
        this.productService.save(product);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    private ModelAndView showEditForm(@PathVariable Long id) {
        Product product = this.productService.findById(id);
        ProductForm productForm = new ProductForm(product.getName(), product.getPrice(), product.getQuantity(), product.getDescription(), null);
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        modelAndView.addObject("product", product);
        modelAndView.addObject("productForm", productForm);
        return modelAndView;
    }
    @GetMapping("/delete/{id}")
    private ModelAndView showDeleteForm(@PathVariable Long id) {
        Product product = this.productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product/delete");
        modelAndView.addObject("product", product);
        return modelAndView;
    }
    @PostMapping("/delete/{id}")
    private ModelAndView deleteProduct(@PathVariable Long id){
        Product product = this.productService.findById(id);
        File file = new File(fileUpload + product.getImage());
        if (file.exists()){
            file.delete();
        }
        this.productService.delete(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }
    @PostMapping("/edit/{id}")
    private ModelAndView editProduct(@PathVariable Long id, @ModelAttribute ProductForm productForm) {
        MultipartFile multipartFile = productForm.getImage();
        Product product = this.productService.findById(id);
        String image;
        if (multipartFile.getSize() == 0) {
            image = product.getImage();
        } else {
            String fileName = multipartFile.getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            fileName = currentTime + fileName;
            try {
                FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            image = fileName;
        }
        Product product1 = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(), productForm.getQuantity(), productForm.getDescription(), image);
        this.productService.save( product1);
        ModelAndView modelAndView = new ModelAndView("redirect:/products");
        return modelAndView;
    }


}
