package com.example.mathengerapi.security.configuration;

import com.example.mathengerapi.exceptions.UnAuthorizedException;
import com.example.mathengerapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        if (!StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        } else if (tokenProvider.validateToken(jwt)) {
            try {
                refreshSession(request, jwt);
            } catch (Exception e) {
                e.printStackTrace();
                SecurityContextHolder.clearContext();
                response.sendError(401,"Could not set user" + " authentication in security context while "
                        + "checking authentication token! Try to log out " + "and log in again.");
                return;
            }
        } else {
            SecurityContextHolder.clearContext();
            response.sendError(401, "Authentication token not valid! " +
                    "Try to sign out and sign in again.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7, bearerToken.length());
            return token.equals("Invalid") ? null : token;
        }
        return null;
    }

    private void refreshSession(HttpServletRequest request, String jwt) {
        Long userId = tokenProvider.getUserIdFromJWT(jwt);

        UserDetails userDetails = userService
                .loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities()
                );
        authentication.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request)
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}
