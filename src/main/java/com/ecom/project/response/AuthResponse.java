package com.ecom.project.response;

import com.ecom.project.domain.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
private String jwt;

    public String getJwt(String jwt) {
        return this.jwt;
    }

    public String getMessage() {
        return message;
    }

    public USER_ROLE getRole() {
        return role;
    }




    private String message;
private USER_ROLE role;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRole(USER_ROLE userRole) {
    }
}
