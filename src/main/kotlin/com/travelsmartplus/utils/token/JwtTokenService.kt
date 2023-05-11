package com.travelsmartplus.utils.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * JwtTokenService provides functions for generating JWT tokens with claims and expiration date.
 * @author Gabriel Salas
 * @return The generated JWT token as a string.
 */

class JwtTokenService {

    private val secret: String = System.getenv("JWT_SECRET")
    private val issuer: String = System.getenv("JWT_ISSUER")
    private val audience: String = System.getenv("JWT_AUDIENCE")

    // Generates a new token with claims and expiration date
    fun generate(vararg claims: TokenClaim, expiration: Long): String {
        var token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + expiration))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(secret))
    }
}
