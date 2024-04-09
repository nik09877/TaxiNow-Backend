package com.taxinow.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String jwt = request.getHeader(JwtSecurityContext.JWT_HEADER);
       if(jwt != null){
           try{
               //extract the word bearer [ex: Bearer feijrgeljdfsd]
               jwt = jwt.substring(7);

               //generate key
               SecretKey key = Keys.hmacShaKeyFor(JwtSecurityContext.JWT_KEY.getBytes());

               Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(jwt).getBody();

               String username = String.valueOf(claims.get("email"));
               String authorities = (String)claims.get("authorities");
               List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
               Authentication auth = new UsernamePasswordAuthenticationToken(username,null,auths);

               SecurityContextHolder.getContext().setAuthentication(auth);
           }
           catch (Exception e){
               throw new BadCredentialsException("Invalid Token received...");
           }
       }
       //go to next filter
       filterChain.doFilter(request,response);
    }

//    protected boolean shouldNotFilter(HttpServletRequest req)throws ServletException{
//        return req.getServletPath().equals("/api/auth/**");
//    }
}
