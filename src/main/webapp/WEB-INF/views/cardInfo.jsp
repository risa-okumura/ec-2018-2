<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

			<form:form action="${pageContext.request.contextPath}/cardConfirm" method="post" modelAttribute="cardInfoForm">
			クレジットカード番号：<form:input path="card_number"/><br>
			有効期限：<form:input path="card_exp_month"/><form:input path="card_exp_year"/><br>
			カード名義人：<form:input path="card_name"/><br>
			セキュリティコード：<form:input path="card_cvv"/><br>
			<input type="hidden" name="user_id" value="1">
			<input type="hidden" name="order_number" value="1">
			<input type="hidden" name="amount" value="100000">
			<input type="submit" value="送信">
			</form:form>

</body>
</html>