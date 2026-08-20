package com.kvn.schoolinvoices;

import com.kvn.schoolinvoices.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService {


  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;


  @Transactional
  public AppUser register(
          RegisterRequest request) {


    if (
            userRepository.existsByEmail(
                    request.getEmail()
            )
    ) {

      throw new UserAlreadyExistsException(
              "Email is already in use"
      );
    }


    Set<Role> roles =
            new HashSet<>();


    Role schoolAdminRole =
            roleRepository
                    .findByName(
                            RoleName.ROLE_SCHOOL_ADMIN
                    )
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "ROLE_SCHOOL_ADMIN not configured"
                            )
                    );


    roles.add(schoolAdminRole);


    AppUser user =
            AppUser.builder()

                    .fullName(
                            request.getFullName()
                    )

                    .email(
                            request.getEmail()
                    )

                    /*
                     * Temporary only.
                     * Cognito should eventually
                     * own the password.
                     */
                    .password(
                            passwordEncoder.encode(
                                    request.getPassword()
                            )
                    )

                    .roles(roles)

                    .role("schooladmin")

                    .build();


    return userRepository.save(user);
  }



}