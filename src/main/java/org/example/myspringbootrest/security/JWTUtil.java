package org.example.myspringbootrest.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    @Value("${subject}")
    private String subject;

    @Value("${issuer}")
    private String issuer;

    @Value("${claim}")
    private String claim;

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject(subject)
                .withClaim(claim, username)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(secret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();

        DecodedJWT verify = verifier.verify(token);

        return verify.getClaim(claim).asString();
    }
}