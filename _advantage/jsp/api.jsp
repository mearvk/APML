<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="API"/>
<c:set var="active" value="api"/>
<%@ include file="includes/header.jspf" %>
<h1>API</h1>
<p class="lead">Every page on this site, in HTML, JSP and servlet form, reads the same read-only JSON API.</p>
<c:set var="api" value="${pageContext.request.contextPath}/api/v1"/>
<table>
  <thead><tr><th scope="col">Method and path</th><th scope="col">Returns</th></tr></thead>
  <tbody>
    <tr><td><a href="${api}"><code>GET /api/v1</code></a></td><td>Index of resources</td></tr>
    <tr><td><a href="${api}/health"><code>GET /api/v1/health</code></a></td><td>Liveness</td></tr>
    <tr><td><a href="${api}/technologies"><code>GET /api/v1/technologies</code></a></td><td>The six strands, in order</td></tr>
    <tr><td><code>GET /api/v1/technologies/{slug}</code></td><td>One strand: question, principle, owner claims, open items, hand-off</td></tr>
    <tr><td><a href="${api}/terms"><code>GET /api/v1/terms</code></a></td><td>Sprung terms awaiting formal definition</td></tr>
    <tr><td><a href="${api}/knit"><code>GET /api/v1/knit</code></a></td><td>The loop (hand-offs) and the fold (pairs 1-6, 2-5, 3-4)</td></tr>
    <tr><td><a href="${api}/openapi.json"><code>GET /api/v1/openapi.json</code></a></td><td>OpenAPI 3.0 description</td></tr>
  </tbody>
</table>
<p>Errors use one shape: <code>{"error":{"code":"not_found","message":"..."}}</code>. Other methods return 405.</p>
<div class="actions">
  <a class="btn btn-primary" href="${pageContext.request.contextPath}/console">Open the console</a>
</div>
<%@ include file="includes/footer.jspf" %>
