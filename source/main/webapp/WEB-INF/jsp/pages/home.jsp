<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%@ include file="/WEB-INF/jsp/layout/header.jspf" %>
<section class="page page-home">
  <h1>Home</h1>
  <!-- PLACEHOLDER: replace with the text from the Wix "Home" page. -->
  <p class="lead"><c:out value="${site['site.tagline']}"/></p>
  <p><a class="btn btn-primary" href="${pageContext.request.contextPath}/page/agencies">Explore the Agencies</a>
     <a class="btn btn-ghost" href="${pageContext.request.contextPath}/page/contact">Contact</a></p>
</section>
<%@ include file="/WEB-INF/jsp/layout/footer.jspf" %>
