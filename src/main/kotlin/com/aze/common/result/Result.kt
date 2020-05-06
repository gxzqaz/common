package com.aze.common.result

import com.baomidou.mybatisplus.core.metadata.IPage
import java.io.Serializable


object ResultUtil {

    /**
     * 成功
     */
    fun ok(data: Any = Any()): Result {
        return Result(success = true, showType = 0, errorCode = null, errorMessage = null, data = data)
    }

    /**
     * 错误
     */
    fun error(errorMessage: String?): Result {
        return Result(success = false, showType = 2, errorCode = null, errorMessage = errorMessage)
    }

    /**
     * 错误
     */
    fun error(errorMessage: String?, errorCode: String?): Result {
        return Result(success = false, showType = 2, errorCode = errorCode, errorMessage = errorMessage)
    }

    /**
     * 错误
     */
    fun error(showType: Int, errorMessage: String?, errorCode: String?): Result {
        return Result(success = false, showType = showType, errorCode = errorCode, errorMessage = errorMessage)
    }

    /**
     * 提示用
     */
    fun info(infoMessage: String): Result {
        return Result(success = false, showType = 4, errorCode = null, errorMessage = infoMessage)
    }
}

data class Result(
        var success: Boolean,
        var showType: Int,
        var errorCode: String?,
        var errorMessage: String?,
        var data: Any = Any()) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
}

data class PageResult constructor(
        var current: Long = 1,
        var pageSize: Long = 10,
        var total: Long = 0,
        var totalPage: Long = 1,
        var list: List<Any> = emptyList()) {

    companion object {
        private const val serialVersionUID = 1L

        fun parse(iPage: IPage<out Any>): PageResult {
            return PageResult(iPage.current, iPage.size, iPage.total, iPage.pages, iPage.records)
        }

        fun parse(iPage: IPage<out Any>, list: List<Any>): PageResult {
            return PageResult(iPage.current, iPage.size, iPage.total, iPage.pages, list)
        }

        fun parse(current: Long, pageSize: Long, total: Long, totalPage: Long, list: List<Any> = emptyList()): PageResult {
            return PageResult(current = current, pageSize = pageSize, total = total, totalPage = totalPage, list = list)
        }
    }
}


enum class ShowTypeEnum(val type: Int) {
    SILENT(0),
    WARN_MESSAGE(1),
    ERROR_MESSAGE(2),
    NOTIFICATION(4),
    REDIRECT(9),
}

enum class ErrorCodeEnum(val code: String) {
    DEFAULT("1"),
    NOT_LOGIN("401"),
    UNAUTHORIZED("403"),
}