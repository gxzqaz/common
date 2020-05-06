package com.aze.common.service

import cn.hutool.core.util.StrUtil
import com.aze.common.exception.BizException
import com.aze.common.util.JsonUtil
import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import kotlin.reflect.KClass


@Service
class RedisService @Autowired constructor(
        private val redisTemplate: RedisTemplate<String, Any>) {

    private val objectMapper = JsonUtil.mapper()

    fun set(key: String, value: Any) {
        if (StrUtil.isBlank(key)) {
            throw BizException("key不能为空")
        }
        redisTemplate.execute {
            it.set(key.toByteArray(), JsonUtil.mapper().writeValueAsBytes(value))
        }
    }

    fun <T> get(key: String, clazz: Class<T>): T? {
        return redisTemplate.execute {
            JsonUtil.read(it.get(key.toByteArray()), clazz)
        }
    }

    fun <T> get(key: String, type: TypeReference<T>): T? {
        return redisTemplate.execute {
            objectMapper.readValue(it.get(key.toByteArray()), type)
        }
    }

    fun <T : Any> get(key: String, clazz: KClass<T>): T? {
        return redisTemplate.execute {
            objectMapper.readValue(it.get(key.toByteArray()), clazz.java)
        }
    }
}
