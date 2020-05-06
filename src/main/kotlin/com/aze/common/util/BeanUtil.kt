package com.aze.common.util

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import cn.hutool.core.util.ReflectUtil
import com.aze.common.exception.BizException
import kotlin.reflect.full.memberProperties

val DefaultCopyOptions: CopyOptions = CopyOptions.create().ignoreError().ignoreCase().ignoreNullValue()

fun anyIsNull(vararg args: Any?): Boolean {
    for (any in args) {
        if (any == null) {
            return true
        }
    }
    return false
}


fun anyNotNull(vararg anys: Any?): Boolean {
    if (anys.isEmpty()) {
        throw BizException("传入的长度不能为空")
    }
    for (any in anys) {
        if (any != null) {
            return true
        }
    }
    return false
}


/**
 * 判断对象中的字段是否有null
 */
fun objectIsNull(any: Any?): Boolean {
    if (any == null) {
        return true
    }
    val declaredMembers = any.javaClass.kotlin.memberProperties
    for (member in declaredMembers) {
        member.get(any) ?: return true
    }
    return false
}


fun <T> defaultCopy(source: Any?, clazz: Class<T>): T {
    val target = ReflectUtil.newInstance(clazz)
    BeanUtil.copyProperties(source, target, DefaultCopyOptions)
    return target
}
