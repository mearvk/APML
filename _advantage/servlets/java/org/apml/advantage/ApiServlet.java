package org.apml.advantage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Read-only JSON API under /api/v1. Every page in html/, jsp/ and servlets/ reads from here.
 *
 *   GET /api/v1                      index of resources
 *   GET /api/v1/health               liveness
 *   GET /api/v1/technologies         the six strands, in order
 *   GET /api/v1/technologies/{slug}  one strand
 *   GET /api/v1/terms                Sprung terms awaiting formal definition
 *   GET /api/v1/knit                 the loop and the fold
 *   GET /api/v1/openapi.json         OpenAPI 3.0 description
 */
@WebServlet(name = "ApiServlet", urlPatterns = "/api/v1/*")
public class ApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Catalog catalog() {
        return (Catalog) getServletContext().getAttribute("catalog");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        if (path.endsWith("/") && path.length() > 1) path = path.substring(0, path.length() - 1);
        String base = req.getContextPath() + "/api/" + Catalog.API_VERSION;

        if (path.isEmpty() || path.equals("/")) {
            send(resp, 200, index(base));
        } else if (path.equals("/health")) {
            send(resp, 200, Map.of("status", "ok"));
        } else if (path.equals("/technologies")) {
            List<Object> out = new ArrayList<>();
            for (Technology t : catalog().getTechnologies()) out.add(summary(t, base));
            send(resp, 200, Map.of("technologies", out));
        } else if (path.startsWith("/technologies/")) {
            String slug = path.substring("/technologies/".length());
            Technology t = slug.matches("[a-z]+") ? catalog().find(slug) : null;
            if (t == null) error(resp, 404, "not_found", "No technology with that name.");
            else send(resp, 200, detail(t, base));
        } else if (path.equals("/terms")) {
            List<Object> out = new ArrayList<>();
            for (Term t : catalog().getTerms()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("name", t.getName());
                m.put("ownerDescription", t.getOwnerDescription());
                m.put("status", t.getStatus());
                out.add(m);
            }
            send(resp, 200, Map.of("owner", "sprung", "terms", out));
        } else if (path.equals("/knit")) {
            send(resp, 200, knit());
        } else if (path.equals("/openapi.json")) {
            send(resp, 200, openApi(req.getContextPath() + "/api/" + Catalog.API_VERSION));
        } else {
            error(resp, 404, "not_found", "Unknown API resource.");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String m = req.getMethod();
        if (m.equals("GET") || m.equals("HEAD")) {
            super.service(req, resp);
        } else if (m.equals("OPTIONS")) {
            resp.setHeader("Allow", "GET, HEAD, OPTIONS");
            resp.setStatus(204);
        } else {
            resp.setHeader("Allow", "GET, HEAD, OPTIONS");
            error(resp, 405, "method_not_allowed", "This API is read-only.");
        }
    }

    private Map<String, Object> index(String base) {
        Map<String, Object> links = new LinkedHashMap<>();
        links.put("health", base + "/health");
        links.put("technologies", base + "/technologies");
        links.put("terms", base + "/terms");
        links.put("knit", base + "/knit");
        links.put("openapi", base + "/openapi.json");
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("name", "APML Advantage API");
        m.put("version", Catalog.API_VERSION);
        m.put("links", links);
        return m;
    }

    private Map<String, Object> summary(Technology t, String base) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("order", t.getOrder());
        m.put("slug", t.getSlug());
        m.put("name", t.getName());
        m.put("role", t.getRole());
        m.put("href", base + "/technologies/" + t.getSlug());
        return m;
    }

    private Map<String, Object> detail(Technology t, String base) {
        Map<String, Object> m = summary(t, base);
        m.put("question", t.getQuestion());
        m.put("principle", t.getPrinciple());
        m.put("ownerClaims", t.getOwnerClaims());
        m.put("openItems", t.getOpenItems());
        m.put("handsOffTo", t.getHandsOffTo());
        m.put("pairedWith", t.getPairedWith());
        m.put("assumedAlias", catalog().legacySlug(t.getSlug()));
        return m;
    }

    private Map<String, Object> knit() {
        List<Object> loop = new ArrayList<>();
        for (Technology t : catalog().getTechnologies()) {
            Map<String, Object> e = new LinkedHashMap<>();
            e.put("from", t.getSlug());
            e.put("to", t.getHandsOffTo());
            loop.add(e);
        }
        List<Object> fold = new ArrayList<>();
        for (int[] p : new int[][] {{1, 6}, {2, 5}, {3, 4}}) {
            Technology a = catalog().getTechnologies().get(p[0] - 1);
            Technology b = catalog().getTechnologies().get(p[1] - 1);
            fold.add(List.of(a.getSlug(), b.getSlug()));
        }
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("loop", loop);
        m.put("fold", fold);
        return m;
    }

    private Map<String, Object> openApi(String base) {
        Map<String, Object> ok = Map.of("200", Map.of("description", "OK"));
        Map<String, Object> paths = new LinkedHashMap<>();
        for (String p : new String[] {"", "/health", "/technologies", "/technologies/{slug}", "/terms", "/knit"}) {
            paths.put(p.isEmpty() ? "/" : p, Map.of("get", Map.of("responses", ok)));
        }
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("openapi", "3.0.3");
        m.put("info", Map.of("title", "APML Advantage API", "version", "1.0.0"));
        m.put("servers", List.of(Map.of("url", base)));
        m.put("paths", paths);
        return m;
    }

    private void error(HttpServletResponse resp, int status, String code, String message) throws IOException {
        send(resp, status, Map.of("error", Map.of("code", code, "message", message)));
    }

    private void send(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Cache-Control", status == 200 ? "public, max-age=60" : "no-store");
        resp.getWriter().write(Json.write(body));
    }
}
