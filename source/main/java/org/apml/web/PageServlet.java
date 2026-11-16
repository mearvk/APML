package org.apml.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Front controller for /page/&lt;name&gt;. Only names registered in
 * WEB-INF/site.properties are served, which prevents path traversal into
 * other JSPs.
 */
public class PageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> pages = new LinkedHashMap<>();
    private final Properties site = new Properties();

    @Override
    public void init() throws ServletException {
        try (InputStream in = getServletContext().getResourceAsStream("/WEB-INF/site.properties")) {
            if (in == null) {
                throw new ServletException("WEB-INF/site.properties not found");
            }
            site.load(in);
        } catch (IOException e) {
            throw new ServletException(e);
        }
        for (String name : site.getProperty("pages", "home").split(",")) {
            String key = name.trim();
            if (key.matches("[a-z0-9-]+")) {
                pages.put(key, site.getProperty("page." + key + ".title", key));
            }
        }
        getServletContext().setAttribute("site", site);
        getServletContext().setAttribute("navPages", new ArrayList<>(pages.entrySet()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        String name = (path == null || path.equals("/")) ? "home" : path.substring(1);

        if (!pages.containsKey(name)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("pageName", name);
        req.setAttribute("pageTitle", pages.get(name));
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setHeader("X-Frame-Options", "SAMEORIGIN");
        resp.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        req.getRequestDispatcher("/WEB-INF/jsp/pages/" + name + ".jsp").forward(req, resp);
    }
}
