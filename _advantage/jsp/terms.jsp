<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Terms"/>
<c:set var="active" value="terms"/>
<%@ include file="includes/header.jspf" %>
<h1>Sprung terms</h1>
<p class="lead">The owner's own terms for Sprung. They are recorded as described; no formula has been invented for any of them.</p>
<table>
  <thead><tr><th scope="col">Term</th><th scope="col">Owner description</th><th scope="col">Status</th></tr></thead>
  <tbody>
    <c:forEach var="t" items="${applicationScope.catalog.terms}">
      <tr>
        <th scope="row"><c:out value="${t.name}"/></th>
        <td><c:out value="${empty t.ownerDescription ? 'Not yet written out.' : t.ownerDescription}"/></td>
        <td><c:out value="${t.status}"/></td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="includes/footer.jspf" %>
