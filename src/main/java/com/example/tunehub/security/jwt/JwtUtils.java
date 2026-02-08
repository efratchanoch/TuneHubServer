package com.example.tunehub.security.jwt;

import io.jsonwebtoken.security.Keys;
import com.example.tunehub.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "securitySample");
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();

    }

    public Long getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class);
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid jwt token " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("jwt is expired " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Signature is wrong " + e.getMessage());
        }
        return false;
    }

    public String generateToken(CustomUserDetails userPrincipal) {
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86400000))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }



    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode("=============================================sec=============================================================================================================================="));
    }


    public ResponseCookie generateJwtCookie(CustomUserDetails userPrincipal) {
        String jwt = generateToken(userPrincipal);
        return ResponseCookie.from("securitySample", jwt)
                .path("/")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from("securitySample", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }





}
