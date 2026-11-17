# _advantage

An API-centric site for APML, written three ways from one API: plain HTML, JSP and Servlets.

Look and feel: white background, dark grey buttons, orange emphasis, blue as the overall medium.

## Layout

Each source type is its own folder and carries its own CSS and JavaScript.

```
_advantage/
  html/                      plain HTML; reads the API with fetch()
    index.html technologies.html technology.html terms.html api.html
    css/site.css  js/site.js
  jsp/                       JSP; renders on the server from the Catalog, JS only adds the API status badge
    index.jsp technologies.jsp technology.jsp terms.jsp api.jsp
    includes/header.jspf footer.jspf
    css/site.css  js/site.js
  servlets/                  the API and the servlet-rendered console
    java/org/apml/advantage/
      ApiServlet.java          GET /api/v1/*  (read-only JSON)
      ConsoleServlet.java      GET /console   (HTML written by the servlet)
      Catalog.java Technology.java Term.java   the one source of truth for content
      Json.java                small JSON writer, no dependencies
      CatalogListener.java     publishes the Catalog to the servlet context
      SecurityHeadersFilter.java  CSP: same-origin only, no inline script or style
    WEB-INF/web.xml
    css/console.css  js/console.js
  pom.xml                    assembles the three folders into advantage.war
```

`site.css` in `html/` and `jsp/` and `console.css` in `servlets/` share the same color tokens
(`:root` block). Change them in all three together.

## API (read-only, JSON)

| Request | Returns |
|---------|---------|
| `GET /api/v1` | index of resources |
| `GET /api/v1/health` | liveness |
| `GET /api/v1/technologies` | the six strands, in order |
| `GET /api/v1/technologies/{slug}` | question, principle, owner claims, open items, hand-off, pairing |
| `GET /api/v1/terms` | Sprung terms awaiting formal definition |
| `GET /api/v1/knit` | the loop (hand-offs) and the fold (1-6, 2-5, 3-4) |
| `GET /api/v1/openapi.json` | OpenAPI 3.0 description |

Errors: `{"error":{"code":"not_found","message":"..."}}`. Non-GET methods return 405.

## Where the content comes from

`Catalog.java` is built from what the repo already records:

- strand questions and principles: `sleela-source/Strand1..6*.sleela` and `docs/ENGINEERING-PRINCIPLES.md`
- owner claims and open items: `analysis/APML-ANALYSIS.md`
- hand-offs and the fold: `sleela-source/README.md` (`DesignKnit`)
- Sprung terms: `.kiro/steering/apml-framework.md`

Nothing is invented. The "Creative time-force equation" has no description published, because its wording is
still awaiting the owner's approval. Cordial labels from the analysis are the drafter's inference and are left out.

## Build and run

    mvn package        # target/advantage.war
Deploy to Tomcat 10.1+ or Jetty 12 (Jakarta EE 10, Servlet 6), then open `/advantage/`.

| URL | Source type |
|-----|-------------|
| `/advantage/html/index.html` | HTML |
| `/advantage/jsp/index.jsp` | JSP |
| `/advantage/console` | Servlet |
| `/advantage/api/v1` | API |

## Verified so far

- Java sources compile with `-Xlint:all` against Servlet API 6.1 and `--release 17`.
- `ApiServlet` was driven with stubbed requests: all routes, 404 for unknown or malformed slugs, 405 for POST.
- The three JavaScript files pass `node --check`.
- Not yet run: `mvn package` and the JSP pages in a real container (Maven Central was unreachable from the build sandbox).
