package gov.bd.grs_security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long accessTokenExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetailsImpl userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    public String generateAccessToken(
            Map<String, String> extraClaims,
            UserDetailsImpl userDetails
    ) {
        return buildToken(extraClaims, userDetails, accessTokenExpiration, true);
    }

    public String generateRefreshToken(
            Map<String, String> extraClaims,
            UserDetailsImpl userDetails
    ) {
        return buildToken(extraClaims, userDetails, refreshTokenExpiration, false);
    }

    private String buildToken(
            Map<String, String> extraClaims,
            UserDetailsImpl userDetails,
            long expiration,
            Boolean forAccessToken
    ) {
        if (forAccessToken) {
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .claim("userType", userDetails.getUserInformation().getUserType())
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } else {
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }
    }

    public boolean isTokenValid(String token) {
        final String username = extractUsername(token);
        if (username != null) {
            return !isTokenExpired(token);
        } else {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
