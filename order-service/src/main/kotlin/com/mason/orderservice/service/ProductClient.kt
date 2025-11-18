package com.mason.orderservice.service

import com.mason.orderservice.model.ProductDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(name = "product-service")
interface ProductClient {

    @GetMapping("/api/products/{id}")
    fun getProductById(@PathVariable id: Long): ProductDto

    @GetMapping("/api/products")
    fun getAllProducts(): List<ProductDto>
}