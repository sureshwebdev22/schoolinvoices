package com.kvn.schoolinvoices;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {


    private final CognitoJwtAuthenticationConverter
            cognitoJwtAuthenticationConverter;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {


        http

                .csrf(csrf ->
                        csrf.disable()
                )


                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )


                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                .authorizeHttpRequests(auth -> auth

                        /*
                         * Public endpoints
                         */
                        .requestMatchers(
                                "/api/public/**"
                        ).permitAll()


                        /*
                         * Cognito handles login.
                         *
                         * Keep register only if you still
                         * use your backend registration API.
                         */
                        .requestMatchers(
                                "/api/auth/register"
                        ).permitAll()


                        /*
                         * Students
                         */
                        .requestMatchers(
                                "/api/students",
                                "/api/students/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "SCHOOL_ADMIN",
                                "PARENT"
                        )


                        /*
                         * Users
                         */
                        .requestMatchers(
                                "/api/user/**"
                        )
                        .hasAnyRole(
                                "USER",
                                "ADMIN"
                        )


                        /*
                         * School admin
                         */
                        .requestMatchers(
                                "/api/schooladmin/**"
                        )
                        .hasAnyRole(
                                "SCHOOL_ADMIN",
                                "PARENT"
                        )


                        /*
                         * Payments
                         */
                        .requestMatchers(
                                "/api/payments/**"
                        )
                        .hasAnyRole(
                                "SCHOOL_ADMIN",
                                "PARENT"
                        )


                        /*
                         * Everything else
                         */
                        .anyRequest()
                        .authenticated()
                )


                /*
                 * Cognito JWT validation
                 */
                .oauth2ResourceServer(oauth2 ->

                        oauth2.jwt(jwt ->

                                jwt.jwtAuthenticationConverter(
                                        cognitoJwtAuthenticationConverter
                                )

                        )

                );


        return http.build();
    }


    @Bean
    public CorsConfigurationSource
    corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200", "http://localhost:8080",
                        "http://13.204.43.112", "http://ec2-13-204-43-112.ap-south-1.compute.amazonaws.com", "http://13.204.43.112:8080", "http://ec2-13-204-43-112.ap-south-1.compute.amazonaws.com:8080"));

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"));

        configuration.setAllowedHeaders(
                List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}