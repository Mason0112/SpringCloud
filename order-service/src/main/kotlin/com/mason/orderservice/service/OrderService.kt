package com.mason.orderservice.service

import org.springframework.stereotype.Service

@Service
class OrderService (
    val productClient: ProductClient
){

    fun testClient(): String {
        val products = productClient.getAllProducts()
        return products.toString()
    }

}