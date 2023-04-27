package com.insurance.monocept.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.insurance.monocept.entity.User;


public class CustomTokenAuthenticationFilter extends OncePerRequestFilter{

	@Value("${jwt.token}")
	private String aidus_token;

	@Autowired
	private JWTAuthenticationToken jwtToken;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader(aidus_token);
		try {
			authUserByToken(token);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		User user = (User) jwtToken.getPrincipal();
	
		if(user == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, jwtToken.getCredentials(), user.getAuthorities());

		if(jwtToken == null){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;        	
		} 
	
		
		/*if(!authService.isTokenValid(ipAddress,token)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;

		}*/
		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	/**
	 * authenticate the user based on token
	 * @return
	 */
	private void authUserByToken(String token) throws Exception{
		jwtToken.setParser(token);
	}


}