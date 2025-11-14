package com.mason.productservice.model.dto

data class ProductDto(
    val id: Long?,
    val name: String,
    val price: Double,
    val stock: Int,
    val description: String?
)
