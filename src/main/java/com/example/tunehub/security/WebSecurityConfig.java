package com.example.tunehub.security;


import com.example.tunehub.security.jwt.AuthEntryPointJwt;
import com.example.tunehub.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.servlet.View;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Qualifier("customUserDetailsService")
    CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, View error) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
                    corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:4200"));

                    corsConfiguration.setAllowedMethods(List.of("*"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                                auth.requestMatchers("/h2-console/**").permitAll()
                                        .requestMatchers("/ws-notifications/**").authenticated()
                                        .requestMatchers("/api/post/paged").permitAll()
                                        .requestMatchers("/api/post/**").authenticated()
                                        .requestMatchers("/api/sheetMusic/**").authenticated()
                                        .requestMatchers("/api/sheetMusicCategory/**").authenticated()
                                        .requestMatchers("/api/instrument/**").authenticated()
                                        .requestMatchers("/api/users/countActive").permitAll()
                                        .requestMatchers("/api/users/signIn").permitAll()
                                        .requestMatchers("/api/users/signUp").permitAll()
                                        .requestMatchers("/api/users/**").authenticated()
                                        .requestMatchers("/api/users/signOut").authenticated()
                                        .requestMatchers("/api/teacher/**").permitAll() //
                                        .requestMatchers("/api/comment/**").authenticated()
                                        .requestMatchers("/api/interaction/**").authenticated()
                                        .requestMatchers("/api/notification/**").authenticated()
                                        .requestMatchers("api/role/**").permitAll() //
                                        .requestMatchers("/api/users/chat").permitAll()
                                        .requestMatchers("/api/search/**").authenticated()
                                   //     .requestMatchers("/api/post/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")

                );

        http.headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                        "frame-ancestors 'self' http://localhost:4200;"
                ))
        );
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
