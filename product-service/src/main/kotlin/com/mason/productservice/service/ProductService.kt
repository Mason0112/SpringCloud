package com.mason.productservice.service

import com.mason.productservice.mapper.toDto
import com.mason.productservice.model.dto.ProductDto
import com.mason.productservice.model.entity.Product
import com.mason.productservice.repository.ProductRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit


@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) {
    private val cachePrefix = "product:"
    private val cacheExpiration = 30L // 30 分鐘

    fun getProductById(id: Long): ProductDto? {
        val cacheKey = "$cachePrefix$id"

        // 1. 先查 Redis
        val cachedProduct = redisTemplate.opsForValue().get(cacheKey) as? Product
        if (cachedProduct != null) {
            println("從 Redis 快取獲取商品: $id")
            return cachedProduct.toDto()
        }

        // 2. Redis 沒有,查資料庫
        println("快取未命中,從資料庫查詢商品: $id")
        val product = productRepository.findById(id).orElse(null) ?: return null

        // 3. 寫入 Redis
        redisTemplate.opsForValue().set(cacheKey, product, cacheExpiration, TimeUnit.MINUTES)

        return product.toDto()
    }

    fun getAllProducts(): List<ProductDto> {
        return productRepository.findAll().map { product -> product.toDto() }
    }

    fun createProduct(product: Product): ProductDto {
        val savedProduct = productRepository.save(product)

        // 寫入快取
        val cacheKey = "$cachePrefix${savedProduct.id}"
        redisTemplate.opsForValue().set(cacheKey, savedProduct, cacheExpiration, TimeUnit.MINUTES)

        return savedProduct.toDto()
    }

    fun updateProduct(id: Long, productDetails: ProductDto): ProductDto? {
        val existingProduct = productRepository.findById(id).orElse(null) ?: return null

        val updatedProduct = existingProduct.copy(
            name = productDetails.name,
            price = productDetails.price,
            stock = productDetails.stock,
            description = productDetails.description
        )

        val savedProduct = productRepository.save(updatedProduct)

        // 更新快取
        val cacheKey = "$cachePrefix$id"
        redisTemplate.opsForValue().set(cacheKey, savedProduct, cacheExpiration, TimeUnit.MINUTES)

        return savedProduct.toDto()
    }

    fun deleteProduct(id: Long): Boolean {
        if (!productRepository.existsById(id)) {
            return false
        }

        productRepository.deleteById(id)

        // 刪除快取
        val cacheKey = "$cachePrefix$id"
        redisTemplate.delete(cacheKey)

        return true
    }
}