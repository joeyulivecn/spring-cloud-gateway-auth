package com.love.plus.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Slf4j
public class JwtTokenProvider {
    @Value("${love.plus.app.jwtSecret}")
    private static final String SECRET = "67ef9c880f8e33966d60d612d32562301f343c3b";

    private static final String ISS = "love+";

    private static final long EXPIRATION = 3600L;

    public String generateJWTToken(Long userId, String userName) {
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String token = JWT.create()
                .withIssuer(ISS)
                .withIssuedAt(now)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION * 1000 * 24))
                .withClaim("uid", userId)
                .withClaim("name", userName)
                .sign(algorithm);
        return token;
    }

    public String verifyJWT(String token) {
        String userId = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISS)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            Claim userIdClaim = jwt.getClaim("uid");
            userId = userIdClaim.asLong().toString();

        } catch (JWTVerificationException e) {
            log.error(e.getMessage(), e);
        }

        return userId;
    }
}
