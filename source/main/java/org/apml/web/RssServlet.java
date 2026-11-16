package org.apml.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Serves assets/rss/feed.xml at /rss.xml with the proper content type. */
public class RssServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try (InputStream in = getServletContext().getResourceAsStream("/assets/rss/feed.xml")) {
            if (in == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.setContentType("application/rss+xml");
            resp.setCharacterEncoding("UTF-8");
            try (OutputStream out = resp.getOutputStream()) {
                in.transferTo(out);
            }
        }
    }
}
