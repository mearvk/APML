<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Error | APML</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/images/favicon.svg" type="image/svg+xml">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/fonts.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/site.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/buttons.css">
</head>
<body>
<main class="page" id="main">
  <h1><c:out value="${requestScope['jakarta.servlet.error.status_code']}" default="Error"/></h1>
  <p>The request could not be completed.</p>
  <p><a class="btn btn-primary" href="${pageContext.request.contextPath}/page/home">Home</a></p>
</main>
</body>
</html>
