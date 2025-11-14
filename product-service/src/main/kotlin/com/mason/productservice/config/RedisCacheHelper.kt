package com.mason.productservice.config

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisCacheHelper(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    /**
     * 通用的取快取方法
     */
    fun <T> get(key: String, clazz: Class<T>): T? {
        return try {
            val cached = redisTemplate.opsForValue().get(key)
            when {
                cached == null -> null
                clazz.isInstance(cached) -> clazz.cast(cached)
                else -> {
                    println("警告: 快取類型不匹配. 預期: $clazz, 實際: ${cached::class.java}")
                    null
                }
            }
        } catch (e: Exception) {
            println("讀取快取失敗: ${e.message}")
            null
        }
    }

    fun set(key: String, value: Any, timeout: Long = 30, unit: TimeUnit = TimeUnit.MINUTES) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit)
        } catch (e: Exception) {
            println("寫入快取失敗: ${e.message}")
        }
    }

    fun delete(key: String): Boolean {
        return try {
            redisTemplate.delete(key)
        } catch (e: Exception) {
            println("刪除快取失敗: ${e.message}")
            false
        }
    }
}