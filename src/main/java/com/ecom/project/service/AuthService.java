package com.ecom.project.service;

import com.ecom.project.request.LoginRequest;
import com.ecom.project.response.AuthResponse;
import com.ecom.project.response.SignUpRequest;
import org.springframework.stereotype.Service;

public interface AuthService {
    void sentLoginOtp(String email)throws Exception;
String createUser(SignUpRequest req) throws Exception;
AuthResponse signing(LoginRequest req)throws Exception;
}
