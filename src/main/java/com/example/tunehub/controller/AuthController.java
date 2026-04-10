package com.example.tunehub.controller;

import com.example.tunehub.dto.user.UsersProfileDTO;
import com.example.tunehub.security.jwt.JwtUtils;
import com.example.tunehub.service.UsersService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${google.clientId}")
    private String googleClientId;

    private UsersService usersService;
    private JwtUtils jwtUtils;

    @Autowired
    public AuthController(UsersService usersService, JwtUtils jwtUtils) {
        this.usersService = usersService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String idTokenString = payload.get("idToken");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload p = idToken.getPayload();

                String email = p.getEmail();
                String name = (String) p.get("name");
                String pictureUrl = (String) p.get("picture");

                UsersService.GoogleLoginResult result = usersService.processGoogleUser(email, name, pictureUrl);


                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, result.cookie.toString())
                        .body(result.profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google Token");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}