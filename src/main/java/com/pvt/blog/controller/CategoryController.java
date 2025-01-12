package com.pvt.blog.controller;

import com.pvt.blog.pojo.Category;
import com.pvt.blog.service.ICategoryService;
import com.pvt.blog.utils.ResultResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LIWEI
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private ICategoryService categoryService;

    @GetMapping
    public ResultResponse<List<Category>> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/root")
    public ResultResponse<List<Category>> getRootCategories() {
        return categoryService.getRootCategories();
    }

    @GetMapping("/count")
    public ResultResponse<Long> getCategoryCount() {
        return categoryService.getCategoryCount();
    }

    @GetMapping("/hot")
    public ResultResponse<List<Category>> getHotCategories() {
        return categoryService.getHotCategories();
    }
}
