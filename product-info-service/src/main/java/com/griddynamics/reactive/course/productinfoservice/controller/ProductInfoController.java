package com.griddynamics.reactive.course.productinfoservice.controller;

import com.griddynamics.reactive.course.productinfoservice.domain.Product;
import com.griddynamics.reactive.course.productinfoservice.service.ProductInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/productInfoService")
@RequiredArgsConstructor
public class ProductInfoController {

    private final ProductInfoService productInfoService;


    @GetMapping(value = "/product/names/{productCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProductNamesByProductCode(@PathVariable String productCode) {
        return productInfoService.getProductNamesByProductCode(productCode);
    }
}
