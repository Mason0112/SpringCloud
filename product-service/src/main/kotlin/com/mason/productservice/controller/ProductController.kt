package com.mason.productservice.controller

import com.mason.productservice.model.dto.ProductDto
import com.mason.productservice.model.entity.Product
import com.mason.productservice.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getAllProducts(): List<ProductDto> {
        return productService.getAllProducts()
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductDto> {
        return productService.getProductById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()

    }

    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<ProductDto> {
        return productService.createProduct(product).let {
            ResponseEntity.ok(it)
        }
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody productDetails: ProductDto
    ): ResponseEntity<ProductDto> {
        return productService.updateProduct(id, productDetails)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()

    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        return if (productService.deleteProduct(id)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}