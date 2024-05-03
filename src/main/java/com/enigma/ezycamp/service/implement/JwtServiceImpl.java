package com.enigma.ezycamp.service.implement;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.ezycamp.dto.response.JwtClaims;
import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("ezycamp.jwt.secret_key")
    private final String JWT_SECRET;
    @Value("ezycamp.jwt.issuer")
    private final String ISSUER;
    @Value("ezycamp.jwt.expirationInSecond")
    private final Long JWT_EXPIRATION;

    @Override
    public String generateToken(UserAccount account) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            return JWT.create().withSubject(account.getId())
                    .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuedAt(Instant.now()).withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                    .withIssuer(ISSUER).sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error dalam generate JWT token");
        }
    }

    @Override
    public Boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            String jwt = null;
            if (token != null && token.startsWith("Bearer ")) {
                jwt = token.substring(7);
            }
            jwtVerifier.verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    @Override
    public JwtClaims getClaimsByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            String jwt = null;
            if (token != null && token.startsWith("Bearer ")) {
                jwt = token.substring(7);
            }
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            return JwtClaims.builder().userAccountId(decodedJWT.getSubject())
                    .roles(decodedJWT.getClaim("roles").asList(String.class)).build();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
