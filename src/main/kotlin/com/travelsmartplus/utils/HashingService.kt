package com.travelsmartplus.utils

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

/**
 * HashingService provides functions for generating and verifying hashed values using salt.
 * @author Gabriel Salas
 */

class HashingService {

    // Generates salt and hash using user password
    fun generate(value: String, saltLength: Int = 32): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHax = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHax$value")
        return SaltedHash(
            hash = hash,
            salt = saltAsHax
        )
    }

    // Verifies input
    fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + value) == saltedHash.hash
    }
}
