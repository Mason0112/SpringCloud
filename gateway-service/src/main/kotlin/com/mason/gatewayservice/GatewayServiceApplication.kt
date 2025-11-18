package com.mason.gatewayservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse


@SpringBootApplication
@EnableDiscoveryClient
class GatewayServiceApplication

@Configuration
class GatewayRoutes {

    @Bean
    fun productServiceRoutes(): RouterFunction<ServerResponse?> {
        return route("productsRoute")
            .GET("/api/products/**", http())
            .before(uri("lb://product-service"))
            .build()
    }

    @Bean
    fun orderServiceRoutes(): RouterFunction<ServerResponse?> {
        return route("ordersRoute")
            .GET("/api/orders/**", http())
            .before(uri("lb://order-service"))
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayServiceApplication>(*args)
}
