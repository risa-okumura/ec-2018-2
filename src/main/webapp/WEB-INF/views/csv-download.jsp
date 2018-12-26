<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<form:form action="${pageContext.request.contextPath}/csvDownload" method="post">
<input type="submit" value="mapperCSVダウンロード"/>
</form:form>
<br>
<form:form action="${pageContext.request.contextPath}/testCsvDownload" method="post">
<input type="submit" value="copyCSVダウンロード"/>
</form:form>
<br>
<form:form action="${pageContext.request.contextPath}/download" method="post">
<input type="submit" value="writerCSVダウンロード"/>
</form:form>


</body>
</html>