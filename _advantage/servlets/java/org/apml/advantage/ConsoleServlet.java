package org.apml.advantage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet-rendered API console at /console. Its CSS and JS live in servlets/css and servlets/js
 * and are served from /servlets/css and /servlets/js.
 */
@WebServlet(name = "ConsoleServlet", urlPatterns = "/console")
public class ConsoleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ctx = req.getContextPath();
        resp.setContentType("text/html;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        PrintWriter w = resp.getWriter();
        w.println("<!DOCTYPE html>");
        w.println("<html lang=\"en\"><head><meta charset=\"UTF-8\">");
        w.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        w.println("<title>API Console | APML Advantage</title>");
        w.println("<link rel=\"stylesheet\" href=\"" + ctx + "/servlets/css/console.css\">");
        w.println("<script defer src=\"" + ctx + "/servlets/js/console.js\"></script>");
        w.println("</head><body>");
        w.println("<header class=\"bar\"><a class=\"brand\" href=\"" + ctx + "/html/index.html\">APML Advantage</a>");
        w.println("<span class=\"medium\">Servlet console</span></header>");
        w.println("<main><h1>API Console</h1>");
        w.println("<p class=\"lead\">Call the read-only API and see the JSON it returns.</p>");
        w.println("<form id=\"call\" data-base=\"" + ctx + "/api/" + Catalog.API_VERSION + "\">");
        w.println("<label for=\"resource\">Resource</label>");
        w.println("<select id=\"resource\">");
        for (String r : new String[] {"", "/health", "/technologies", "/technologies/apml", "/terms", "/knit", "/openapi.json"}) {
            w.println("<option value=\"" + r + "\">GET /api/v1" + r + "</option>");
        }
        w.println("</select>");
        w.println("<button type=\"submit\" class=\"btn btn-primary\">Send</button></form>");
        w.println("<p id=\"status\" class=\"status\" role=\"status\"></p>");
        w.println("<pre id=\"out\" class=\"out\" tabindex=\"0\"></pre>");
        w.println("</main></body></html>");
    }
}
