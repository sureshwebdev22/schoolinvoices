package com.kvn.schoolinvoices;


import com.kvn.schoolinvoices.service.repository.AppUserRepository;
import org.springframework.core.convert.converter.Converter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.oauth2.jwt.Jwt;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CognitoJwtAuthenticationConverter
        implements Converter<
            Jwt,
        AbstractAuthenticationToken> {

    private final AppUserRepository userRepository;

    public CognitoJwtAuthenticationConverter(
            AppUserRepository userRepository) {

        this.userRepository =
                userRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(
            Jwt jwt) {

        String email =
                jwt.getClaimAsString("email");

        String cognitoSub =
                jwt.getSubject();

        AppUser user =
                userRepository
                    .findByCognitoSub(cognitoSub)
                    .orElseThrow(() ->
                        new UsernameNotFoundException(
                            "Application user not found for Cognito sub: "
                            + cognitoSub
                        )
                    );

        List<SimpleGrantedAuthority> authorities =
                user.getRoles()
                    .stream()
                    .map(role ->

                        new SimpleGrantedAuthority(""+

                            role.getName()
                        )

                    )
                    .toList();

        return new UsernamePasswordAuthenticationToken(
                user,
                jwt,
                authorities
        );
    }
}