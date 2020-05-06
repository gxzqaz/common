package com.aze.common.client

import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.StrUtil
import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.auth.COSCredentials
import com.qcloud.cos.http.HttpMethodName
import com.qcloud.cos.internal.SkipMd5CheckStrategy
import com.qcloud.cos.model.GeneratePresignedUrlRequest
import com.qcloud.cos.model.ObjectMetadata
import com.qcloud.cos.model.PutObjectRequest
import com.qcloud.cos.region.Region
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TencentCosClient(
    // 地域名称
    regionName: String,
    secretId: String,
    secretKey: String,
    private val bucketName: String,
    private val prefix: String? = null,
    skipMd5Check: Boolean = true
) {
    private val cosClient: COSClient
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    private val logger = LoggerFactory.getLogger(this.javaClass)


    init {
        // 不校验md5
        System.setProperty(SkipMd5CheckStrategy.DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY, skipMd5Check.toString())
        val cred: COSCredentials = BasicCOSCredentials(secretId, secretKey)
        val region = Region(regionName)
        val clientConfig = ClientConfig(region)
        // 3 生成 cos 客户端。
        cosClient = COSClient(cred, clientConfig)
    }

    fun upload(file: File): String {
        val key = if (prefix != null) {
            "${prefix}/${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}"
        } else {
            "${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}"
        }
        val putObjectRequest = PutObjectRequest(bucketName, key, file)
        val putObjectResult = cosClient.putObject(putObjectRequest)
        return key
    }

    fun upload(input: InputStream, metadata: ObjectMetadata): String {
        val key = if (prefix != null) {
            "${prefix}/${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}"
        } else {
            "${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}"
        }
        val putObjectResult = cosClient.putObject(bucketName, key, input, metadata)
        return key
    }

    /**
     * 显示加上后缀
     */
    fun upload(input: InputStream, suffix: String?, metadata: ObjectMetadata): String {
        val key = if (prefix != null) {
            if (suffix == null) "${prefix}/${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}"
            else "${prefix}/${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}${suffix}"
        } else {
            if (suffix == null) "${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}"
            else "${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}${suffix}"
        }
        val putObjectResult = cosClient.putObject(bucketName, key, input, metadata)
        return key
    }

    fun upload(putObjectRequest: PutObjectRequest): String {
        if (StrUtil.isBlank(putObjectRequest.key)) {
            putObjectRequest.key = "${prefix}/${dateFormat.format(LocalDate.now())}/${IdUtil.objectId()}"
        }
        putObjectRequest.bucketName = bucketName
        val putObjectResult = cosClient.putObject(putObjectRequest)
        return putObjectRequest.key
    }

    /**
     * 获取可以公开访问的地址
     * @param expireSecond 过期时间，秒
     */
    fun getPublicUrl(ossKey: String?, expireSecond: Int): URL {
        if (ossKey == null) {
            logger.warn("ossKey 不能为空")
        }
        val expiredTime = Date(System.currentTimeMillis() + expireSecond * 1000L)
        val req = GeneratePresignedUrlRequest(bucketName, ossKey, HttpMethodName.GET)
        req.expiration = expiredTime
        return cosClient.generatePresignedUrl(req)
    }

    /**
     * 获取可以公开访问的地址，默认5分钟
     * @param expireSecond 过期时间，秒
     */
    fun getPublicUrl(ossKey: String?): URL {
        return getPublicUrl(ossKey, 300)
    }
}
