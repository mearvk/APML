# APML
New Engaging Software for the Net Universe

A JSP and Servlet site (Jakarta EE 10, Servlet 6) migrated from the Wix site
https://mearvk.wixsite.com/bloq.

## Layout
Source lives in `source/` rather than `src/`; `pom.xml` is configured for that.

```
source/main/java/org/apml/web/     PageServlet (/page/*), RssServlet (/rss.xml)
source/main/webapp/
  index.jsp                        redirects to /page/home
  WEB-INF/web.xml, site.properties page registry and site settings
  WEB-INF/jsp/layout/              header.jspf, footer.jspf
  WEB-INF/jsp/pages/               one JSP per page (not directly reachable)
  assets/css js images fonts rss social
  agencies/<name>/{html,css,js}    one folder per Software Agency
docs/DOCTRINE.md                   doctrinal theme (provisional)
docs/ENGINEERING-PRINCIPLES.md     long-form six-strand discipline (draft)
analysis/APML-ANALYSIS.md          page-by-page analysis of the six technologies
```

## Build and run
    mvn package            # produces target/apml.war
Deploy the WAR to Tomcat 10.1+ or Jetty 12, then open `/apml/`.

## Status
Structure, servlets, layout, buttons, DHTML and feed plumbing are in place.
Page text, images, fonts, colors, social links and agency names are
placeholders until the Wix content is supplied.
