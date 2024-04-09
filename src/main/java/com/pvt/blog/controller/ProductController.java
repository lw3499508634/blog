package com.pvt.blog.controller;

import com.pvt.blog.pojo.Product;
import com.pvt.blog.service.ProductService;
import com.pvt.blog.util.ResultResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @GetMapping
    public ResultResponse<List<Product>> getProducts() {
        return productService.getProducts();
    }

    @PostMapping
    public ResultResponse<String> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
}
