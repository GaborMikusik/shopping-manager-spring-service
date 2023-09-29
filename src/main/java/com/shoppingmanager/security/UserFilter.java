package com.shoppingmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserFilter extends OncePerRequestFilter {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String usernameOrEmail = getUsernameOrEmailFromRequest(httpServletRequest);
        String password = getPasswordFromRequest(httpServletRequest);

        if (usernameOrEmail == null || password == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(usernameOrEmail);
        if (!this.passwordEncoder.matches(password, userDetails.getPassword())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getUsernameOrEmailFromRequest(HttpServletRequest request) {
        String usernameOrEmail = request.getHeader("usernameOrEmail-or-email");
        if (StringUtils.hasText(usernameOrEmail))
            return usernameOrEmail;

        return null;
    }

    private String getPasswordFromRequest(HttpServletRequest request) {
        String password = request.getHeader("password");
        if (StringUtils.hasText(password))
            return password;

        return null;
    }

}
