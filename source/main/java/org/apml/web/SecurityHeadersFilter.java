package org.apml.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Adds security headers to every response (pages, static assets, feed and
 * error pages) and a short public cache lifetime to static assets.
 */
public class SecurityHeadersFilter implements Filter {

    /** Everything is same-origin; remote hosts are only ever linked, never loaded. */
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
            if (req.isSecure()) {
                resp.setHeader("Strict-Transport-Security", "max-age=31536000");
            }
            String path = req.getRequestURI().substring(req.getContextPath().length());
            if (path.startsWith("/assets/") || path.startsWith("/agencies/")) {
                resp.setHeader("Cache-Control", "public, max-age=3600");
            }
        }
        chain.doFilter(request, response);
    }
}
