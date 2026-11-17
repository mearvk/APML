<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="t" value="${applicationScope.catalog.bySlug[param.slug]}"/>
<c:if test="${empty t}">${pageContext.response.sendError(404)}</c:if>
<c:if test="${not empty t}">
<c:set var="pageTitle" value="${t.name}"/>
<c:set var="active" value="technologies"/>
<%@ include file="includes/header.jspf" %>
<article>
  <h1><c:out value="${t.name}"/></h1>
  <p class="lead"><c:out value="${t.role}"/></p>
  <h2>Question</h2>
  <p><c:out value="${t.question}"/></p>
  <h2>Principle</h2>
  <p class="emphasis"><c:out value="${t.principle}"/></p>
  <h2>What the owner says it does</h2>
  <ul><c:forEach var="c" items="${t.ownerClaims}"><li><c:out value="${c}"/></li></c:forEach></ul>
  <h2>Still open</h2>
  <ul><c:forEach var="c" items="${t.openItems}"><li><c:out value="${c}"/></li></c:forEach></ul>
  <p>Hands off to: <c:out value="${t.handsOffTo}"/>. Paired with: <c:out value="${t.pairedWith}"/>.</p>
</article>
<p><a class="btn btn-ghost" href="technologies.jsp">All technologies</a>
   <a class="btn btn-ghost" href="${pageContext.request.contextPath}/api/v1/technologies/${t.slug}">This page as JSON</a></p>
<%@ include file="includes/footer.jspf" %>
</c:if>
