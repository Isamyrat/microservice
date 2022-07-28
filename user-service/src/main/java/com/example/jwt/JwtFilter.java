package com.example.jwt;

import com.example.exception.UnauthorizedException;
import com.example.service.impl.UserServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserServiceImpl userServiceImpl;
    private final HandlerExceptionResolver resolver;

    public JwtFilter(JwtUtils jwtUtils,
                     UserServiceImpl userServiceImpl,
                     HandlerExceptionResolver resolver) {
        this.jwtUtils = jwtUtils;
        this.userServiceImpl = userServiceImpl;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.parseJwt(httpServletRequest);
            jwtUtils.validateJwtToken(jwt);
            setUpAuthentication(httpServletRequest, jwt);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (UnauthorizedException e) {
            resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
        }
    }

    private void setUpAuthentication(HttpServletRequest httpServletRequest, String jwt) {
        String username = jwtUtils.getUsernameFromToken(jwt);
        UserDetails user = userServiceImpl.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

