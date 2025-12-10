package com.ecom.project.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenValidator implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		String jwt = req.getHeader("Authorization");

		if (jwt != null && jwt.startsWith("Bearer ")) {

			// remove Bearer <token>
			jwt = jwt.substring(7);

			try {
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

				Claims claims = Jwts.parserBuilder()
						.setSigningKey(key)
						.build()
						.parseClaimsJws(jwt)
						.getBody();

				String email = (String) claims.get("email");
				String authorities = (String) claims.get("authorities");

				List<GrantedAuthority> authList =
						AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(email, null, authList);

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (Exception e) {
				throw new BadCredentialsException("Invalid token: " + e.getMessage());
			}
		}

		chain.doFilter(request, response);
	}
}
