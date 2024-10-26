package r3almx.backend.Auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private static final List<String> EXCLUDED_PATHS = List.of("/auth/register/", "/auth/google/callback/",
            "/auth/token/create",
            "/ws/**");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestPath = request.getServletPath();
        String authorizationHeader = request.getHeader("Authorization");

        try {
            // Bypass authentication for excluded paths
            if (EXCLUDED_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestPath))) {
                chain.doFilter(request, response);
                return;
            }

            // Check for valid Authorization header
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing or invalid Authorization header");
                return;
            }

            // Extract token and decode it
            String token = authorizationHeader.substring(7);
            System.out.println("JWT Token from header: " + token);
            String userEmail = authService.decodeToken(token).get("email", String.class);

            if (userEmail == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Invalid or expired token");
                return;
            }

            // Set authentication context if not already set
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userEmail, token, new ArrayList<>());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("Token set in SecurityContext: " + token);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing the request: " + e.getMessage());
        }
    }
}
