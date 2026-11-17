# APML
New Engaging Software for the Net Universe

A JSP and Servlet site (Jakarta EE 10, Servlet 6) migrated from the Wix site
https://mearvk.wixsite.com/bloq.

## Layout
Source lives in `source/` rather than `src/`; `pom.xml` is configured for that.

```
source/main/java/org/apml/web/     PageServlet (/page/*), RssServlet (/rss.xml),
                                   SecurityHeadersFilter (all responses: CSP, nosniff, framing, HSTS on HTTPS)
source/main/webapp/
  index.jsp                        redirects to /page/home
  WEB-INF/web.xml, site.properties page registry and site settings
  WEB-INF/jsp/layout/              header.jspf, footer.jspf
  WEB-INF/jsp/pages/               one JSP per page (not directly reachable), 404.jsp, error.jsp
  assets/css js images fonts rss social
  agencies/<name>/{html,css,js}    one folder per Software Agency
docs/DOCTRINE.md                   doctrinal theme (provisional)
docs/ENGINEERING-PRINCIPLES.md     long-form six-strand discipline (draft)
analysis/APML-ANALYSIS.md          page-by-page analysis of the six technologies
```

## Build and run
    mvn package            # produces target/apml.war
Deploy the WAR to Tomcat 10.1+ or Jetty 12, then open `/apml/`.

## Behavior
- `PageServlet` fails at startup if `site.properties` registers a bad name, a page without a JSP, or no `home`.
- Unknown pages return 404 (`404.jsp`); any other error uses `error.jsp`.
- The CSP allows same-origin resources only: no inline scripts or styles, fonts self-hosted in `assets/fonts`.
- JSPs do not create sessions.

## Status
Structure, servlets, security headers, error pages, layout, buttons, DHTML and
feed plumbing are complete. Page text, fonts, colors, social links, feed items
and agency names are placeholders until the owner supplies the Wix content
(the Wix site was not retrievable automatically).
