package com.ecom.project.controller;

import com.ecom.project.request.LoginRequest;
import com.ecom.project.response.ApiResponse;
import com.ecom.project.response.AuthResponse;
import com.ecom.project.response.SignUpRequest;
import com.ecom.project.service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.project.domain.USER_ROLE;
import com.ecom.project.entity.VerificationCode;
import com.ecom.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
    private  UserRepository userRepository;

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentLoginOtp(
            @RequestBody VerificationCode req) throws Exception    {

        authService.sentLoginOtp(req.getEmail());

        ApiResponse res = new ApiResponse();
        res.setMessage("otp sent succesfully");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req)
            throws Exception {


        String jwt = authService.createUser(req);
        AuthResponse authResponse=new AuthResponse();
        authResponse.getJwt(jwt);
        authResponse.setMessage("Register Succesfull");
        authResponse.setRole(USER_ROLE.ROLE_CUSTOMER);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) throws Exception {

        AuthResponse authResponse = authService.signing(loginRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }



}
