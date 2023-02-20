package com.travelsmartplus.utils.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService {

    private val secret: String = System.getenv("JWT_SECRET")
    private val issuer: String = System.getenv("JWT_ISSUER")
    private val audience: String = System.getenv("JWT_AUDIENCE")
    private val expiration: Long = 365L * 1000L * 60L * 60L * 24L

    fun generate(vararg claims: TokenClaim): String {
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
