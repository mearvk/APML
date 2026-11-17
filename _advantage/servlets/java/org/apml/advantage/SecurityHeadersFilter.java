package org.apml.advantage;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Same-origin only: no inline script or style, so every page keeps its CSS and JS in files. */
@WebFilter(urlPatterns = "/*")
public class SecurityHeadersFilter implements Filter {
    static final String CSP = "default-src 'self'; img-src 'self' data:; object-src 'none'; "
            + "base-uri 'self'; form-action 'self'; frame-ancestors 'self'";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest req && response instanceof HttpServletResponse resp) {
            resp.setHeader("X-Content-Type-Options", "nosniff");
            resp.setHeader("X-Frame-Options", "SAMEORIGIN");
            resp.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
            resp.setHeader("Content-Security-Policy", CSP);
            if (req.isSecure()) resp.setHeader("Strict-Transport-Security", "max-age=31536000");
        }
        chain.doFilter(request, response);
    }
}
