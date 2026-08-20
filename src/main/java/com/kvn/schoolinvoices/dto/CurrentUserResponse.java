package com.kvn.schoolinvoices.dto;

import java.util.List;

public record CurrentUserResponse(
        String email,
        String cognitoSub,
        List<String> roles
) {
}