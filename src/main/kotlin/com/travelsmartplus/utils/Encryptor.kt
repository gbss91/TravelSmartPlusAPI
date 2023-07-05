package com.travelsmartplus.utils

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Used to encrypt and decrypt sensitive personal data using AES algorithm.
 * @author Gabriel Salas
 */

object Encryptor {

    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private val key = System.getenv("ENCRYPTION_KEY")

    fun encrypt(data: String): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(data.toByteArray())
    }

    fun decrypt(data: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return String(cipher.doFinal(data))
    }

}