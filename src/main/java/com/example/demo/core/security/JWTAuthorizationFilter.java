package com.example.demo.core.security;

import com.example.demo.core.security.helpers.AuthorizationSchemas;
import com.example.demo.core.security.helpers.JwtProperties;
import com.example.demo.domain.user.UserService;
import com.example.demo.domain.user.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.UUID;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private final UserService userService;
  private final Key signingKey;

  public JWTAuthorizationFilter(UserService userService, JwtProperties jwtProperties) {
    this.userService = userService;
    byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    final String path = request.getServletPath();
    final String method = request.getMethod();

    if ("/user/login".equals(path) || "/user/register".equals(path)) {
      return true;
    }

    if (path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs")) {
      return true;
    }

    // CORS Preflight nie filtern
    if (HttpMethod.OPTIONS.matches(method)) {
      return true;
    }

    return false;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    try {
      final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

      if (authHeader == null || !authHeader.startsWith(AuthorizationSchemas.BEARER.toString() + " ")) {
        filterChain.doFilter(request, response);
        return;
      }

      final String rawToken = authHeader.substring((AuthorizationSchemas.BEARER.toString() + " ").length());
      Jws<Claims> jws = Jwts.parserBuilder()
              .setSigningKey(signingKey)
              .build()
              .parseClaimsJws(rawToken);

      String subject = jws.getBody().getSubject();
      if (subject == null || subject.isBlank()) {
        filterChain.doFilter(request, response);
        return;
      }

      UUID userId = UUID.fromString(subject);

      var user = userService.findById(userId);
      var userDetails = new UserDetailsImpl(user);

      var authentication = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }
}
