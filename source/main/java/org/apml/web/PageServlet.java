package org.apml.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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
            site.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            for (String name : site.getProperty("pages", "home").split(",")) {
                String key = name.trim();
                if (!key.matches("[a-z0-9-]+")) {
                    throw new ServletException("Invalid page name in site.properties: '" + key + "'");
                }
                if (getServletContext().getResource("/WEB-INF/jsp/pages/" + key + ".jsp") == null) {
                    throw new ServletException("Registered page has no JSP: " + key);
                }
                pages.put(key, site.getProperty("page." + key + ".title", key));
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }
        if (!pages.containsKey("home")) {
            throw new ServletException("site.properties must register the 'home' page");
        }
        getServletContext().setAttribute("site", site);
        getServletContext().setAttribute("navPages",
                Collections.unmodifiableList(new ArrayList<>(pages.entrySet())));
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
        resp.setHeader("Cache-Control", "no-cache");
        req.getRequestDispatcher("/WEB-INF/jsp/pages/" + name + ".jsp").forward(req, resp);
    }
}
