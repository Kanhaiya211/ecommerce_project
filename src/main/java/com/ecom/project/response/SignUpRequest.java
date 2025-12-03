package com.ecom.project.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
public class SignUpRequest {
	 private String fullName;
	    private String email;
	    private String otp;

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getOtp() {
        return otp;
    }
}
