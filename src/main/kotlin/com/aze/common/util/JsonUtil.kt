package com.aze.common.util


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

object JsonUtil {

    private val objectMapper = jacksonObjectMapper()

    init {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.registerModules(JavaTimeModule(), Jdk8Module())
    }

    fun mapper(): ObjectMapper {
        return objectMapper
    }

    fun write(any: Any?): String? {
        if (any == null) {
            return any
        }
        return objectMapper.writeValueAsString(any)
    }

    fun <T> read(byteArray: ByteArray?, clazz: Class<T>): T? {
        if (byteArray == null) {
            return null
        }
        return objectMapper.readValue(byteArray, clazz)
    }

    fun <T> read(key: String?, clazz: Class<T>): T? {
        if (key == null) {
            return null
        }
        return objectMapper.readValue(key, clazz)
    }


    fun <T> readToList(any: String?): List<T> {
        if (any == null) {
            return listOf()
        }
        val valueTypeRef = object : TypeReference<List<T>>() {}
        return objectMapper.readValue(any, valueTypeRef)
    }

    fun readToMap(any: String?): Map<String, Any> {
        if (any == null) {
            return Collections.emptyMap()
        }
        val valueTypeRef = object : TypeReference<Map<String, Any>>() {}
        return objectMapper.readValue(any, valueTypeRef)
    }
}
