<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
JSP (Java Server Pages) is a server side technology which provides using HTML and Java code in same file.
Its an advanced version of servlets which responsible for a view role in MVC approach.
--%>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome JSP</title>
</head>
<body align="center">
<img src="logo_white.svg" alt="Bobocode" width="500">
<h1><%= "Hello! This is index.jsp page." +
        "<br>You can pass a request to the servlet using <code>http://localhost:8080/hello-servlet</code> URL" %>
</h1>
<br/>
<a href="hello-servlet">Or press this link</a>
</body>
</html>