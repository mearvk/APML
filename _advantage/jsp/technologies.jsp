<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Technologies"/>
<c:set var="active" value="technologies"/>
<%@ include file="includes/header.jspf" %>
<h1>Technologies</h1>
<p class="lead">Read left to right: from understanding to production. The last strand hands back to the first, so the chain closes.</p>
<ul class="cards">
  <c:forEach var="t" items="${applicationScope.catalog.technologies}">
    <li class="card">
      <span class="num">Strand <c:out value="${t.order}"/></span>
      <h3><a href="technology.jsp?slug=${t.slug}"><c:out value="${t.name}"/></a></h3>
      <p><c:out value="${t.role}"/></p>
      <p class="emphasis"><c:out value="${t.principle}"/></p>
    </li>
  </c:forEach>
</ul>
<%@ include file="includes/footer.jspf" %>
