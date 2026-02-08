package com.example.tunehub.security.jwt;

import com.example.tunehub.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService userDetailsService;


//    @Override
//
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//
//        String path = httpServletRequest.getRequestURI();
//
//        if (path.startsWith("/api/users/signin") || path.startsWith("/api/users/signup")) {
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//            return;
//        }
//
//        try {
//
//            String jwt = jwtUtils.getJwtFromCookies(httpServletRequest);
//            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//                String userName = jwtUtils.getUserNameFromJwtToken(jwt);
//                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException("JWT verification failed: " + e.getMessage(), e);
//        }
//
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//
//    private String parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//
//        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
//            return headerAuth.substring(7);
//        }
//
//        return jwtUtils.getJwtFromCookies(request);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        List<String> openEndpoints = List.of(
                "/api/users/signIn",
                "/api/users/signUp",
                "/api/users/countActive"
        );

        if (openEndpoints.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtils.getJwtFromCookies(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            try {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        } else {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }


}
