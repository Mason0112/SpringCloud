package com.mason.orderservice.controller

import com.mason.orderservice.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {

    @GetMapping("/test")
    fun testProductServiceCommunication(): String {
        return orderService.testClient()
    }

}