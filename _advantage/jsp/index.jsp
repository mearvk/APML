<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home"/>
<c:set var="active" value="home"/>
<%@ include file="includes/header.jspf" %>
<h1>One description, from design to operation</h1>
<p class="lead">APML is a structure-first framework: one formal XML-subset description drives design, build, operation and review. Six technologies each own one stretch of the path.</p>
<div class="actions">
  <a class="btn btn-primary" href="technologies.jsp">Explore the six technologies</a>
  <a class="btn btn-ghost" href="api.jsp">Use the API</a>
</div>
<h2>The six strands</h2>
<ul class="cards">
  <c:forEach var="t" items="${applicationScope.catalog.technologies}">
    <li class="card">
      <span class="num">Strand <c:out value="${t.order}"/></span>
      <h3><a href="technology.jsp?slug=${t.slug}"><c:out value="${t.name}"/></a></h3>
      <p><c:out value="${t.role}"/></p>
    </li>
  </c:forEach>
</ul>
<%@ include file="includes/footer.jspf" %>
