package com.insurance.monocept.utility;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.springframework.security.core.GrantedAuthority;

import com.insurance.monocept.entity.User;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtility {
	
	public static String createJWT(User user, String issuer, String subject, Collection<? extends GrantedAuthority> collection) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        HashMap<String, Object> userData = new HashMap<String, Object>();
//        System.out.println("email "+user.getUsername());
//        System.out.println("email "+user.getId());
        userData.put("role", user.getAuthorities());
        userData.put("email", user.getUsername());
        userData.put("userId", user.getId().toString());
        //userData.put("name", user.getFirstName() + " " + user.getLastName());
        
        long nowMillis = System.currentTimeMillis();
      
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(issuer);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
        		.setClaims(userData)
        		.setId(user.getUsername())
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        long ttlMillis1 = 1800000000l;
        //if it has been specified, let's add the expiration
        if (ttlMillis1 >= 0) {
            long expMillis = nowMillis + ttlMillis1;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
	}
}
 