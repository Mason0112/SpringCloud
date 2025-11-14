package com.mason.productservice.mapper

import com.mason.productservice.model.dto.ProductDto
import com.mason.productservice.model.entity.Product

fun Product.toDto() = ProductDto(
    id = this.id,
    name = this.name,
    price = this.price,
    stock = this.stock,
    description = this.description
)

fun ProductDto.toProduct() = Product(
    id = this.id,
    name = this.name,
    price = this.price,
    stock = this.stock,
    description = this.description
)