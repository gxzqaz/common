package com.aze.common.exception

class BizException(message: String?, error: Throwable?) : RuntimeException(message, error) {

    constructor(message: String?) : this(message, null)
}

/**
 * 需要重新刷新token
 */
class NeedRefreshTokenException : RuntimeException() {

}
