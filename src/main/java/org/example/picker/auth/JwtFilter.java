package org.example.picker.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException{

        String authHeader = request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }


            try{
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if(jwtUtil.isTokenValid(token,userDetails)){
                        UsernamePasswordAuthenticationToken authToken
                                = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            catch (ExpiredJwtException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired, login again");
                return;
            }
            catch (JwtException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }

        filterChain.doFilter(request,response);
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        return path.equals("/login") || path.equals("/register");
    }

}
