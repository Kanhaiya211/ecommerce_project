package com.ecom.project.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ecom.project.request.LoginRequest;
import com.ecom.project.response.AuthResponse;
import com.ecom.project.service.EmailService;
import com.ecom.project.utils.OtpUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.project.config.JwtProvider;
import com.ecom.project.domain.USER_ROLE;
import com.ecom.project.entity.Cart;
import com.ecom.project.entity.User;
import com.ecom.project.entity.VerificationCode;
import com.ecom.project.repository.CartRepository;
import com.ecom.project.repository.UserRepository;
import com.ecom.project.repository.VerificationCodeRepository;
import com.ecom.project.response.SignUpRequest;
import com.ecom.project.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

//	 private final  UserService userService;

	    private final VerificationCodeRepository verificationCodeRepository;
	    private final EmailService emailService;
	    private final PasswordEncoder passwordEncoder;
	    private final UserRepository userRepository;

	    private final JwtProvider jwtProvider;
	    private final CustomUserServiceImpl customUserService;
	    private final CartRepository cartRepository;

    @Override
    public void sentLoginOtp(String email) throws Exception {
        String SIGNING_PREFIX="signin_";

        if(email.startsWith(SIGNING_PREFIX)){
            email=email.substring(SIGNING_PREFIX.length());
            User user=userRepository.findByEmail(email);
            if (user==null){
            throw new Exception("user not exist with provided email");
                }
        }
        VerificationCode isExist=verificationCodeRepository.findByEmail(email);

        if(isExist!=null){
            verificationCodeRepository.delete(isExist);

        }
String otp= OtpUtil.generateOtp();

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject="Ecom login otp";
        String text= "your login/signup otp "+otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,text);
    }

    @Override
	public String createUser(SignUpRequest req) throws Exception {
		 String email = req.getEmail();


	        String fullName = req.getFullName();

	        String otp = req.getOtp();

	        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

	        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
	            throw new Exception("wrong otp...");
	        }

	        User user = userRepository.findByEmail(email);

	        if (user == null) {

	            User createdUser = new User();
	            createdUser.setEmail(email);
	            createdUser.setFullName(fullName);
	            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
	            createdUser.setMobile("000000000");
	            createdUser.setPassword(passwordEncoder.encode(otp));

	            System.out.println(createdUser);

	            user = userRepository.save(createdUser);

	            Cart cart = new Cart();
	            cart.setUser(user);
	            cartRepository.save(cart);
	        }


	        List<GrantedAuthority> authorities = new ArrayList<>();

	        authorities.add(new SimpleGrantedAuthority(
	                USER_ROLE.ROLE_CUSTOMER.toString()));


	        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                email, null, authorities);
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        return jwtProvider.generateToken(authentication);
	}

    @Override
    public AuthResponse signing(LoginRequest req) throws Exception {
        String username = req.getEmail();
        String otp = req.getOtp();

        System.out.println(username + " ----- " + otp);

        Authentication authentication=authenticate(username,otp);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");

        Collection<?extends GrantedAuthority>authorities=authentication.getAuthorities();
        String rolename=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(rolename));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
      UserDetails userDetails= customUserService.loadUserByUsername(username);
        System.out.println("sign in userDetails - " + userDetails);
      if(userDetails==null){
          throw new BadCredentialsException("Invalid username or Password");
      }

      VerificationCode verificationCode=verificationCodeRepository.findByEmail(username);
      if (verificationCode==null){
          throw new BadCredentialsException("wrong otp");
      }
        return new UsernamePasswordAuthenticationToken(userDetails,
                null,userDetails.getAuthorities());
    }

}
