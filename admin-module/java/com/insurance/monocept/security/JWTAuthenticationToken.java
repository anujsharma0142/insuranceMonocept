package com.insurance.monocept.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationToken extends AbstractAuthenticationToken{
    
	private static final long serialVersionUID = 1L;

    @Autowired
    private JWTParser parser;
 
    public JWTAuthenticationToken() throws AuthenticationServiceException{

        super(null);
        super.setAuthenticated(true); // must use super, as we override
    }
    
    public Object getCredentials() {
        return "";
    }
 
    public Object getPrincipal() {
        return parser.getSub();
    }
    
    public void setParser(String token){
    	parser.setToken(token);
    }
}