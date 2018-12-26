<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

		<!-- login form -->
		<div class="row">
			<div class="col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-10 col-xs-12">
				<div class="well">
					<div class="error" style="color:red"><c:out value="${loginError}"></c:out></div>
					<form:form action="${pageContext.request.contextPath}/login">
						<fieldset>
							<legend>
								ログイン
							</legend>
							<label
									class="control-label" style="color: red" for="inputError"></label>
							<div class="form-group">
								<label for="inputEmail">メールアドレス:</label>
								<input type="text" name="email" id="email" class="form-control"
									placeholder="Email">
							</div>
							<div class="form-group">
								<label for="inputPassword">パスワード:</label>
								<input type="password" name="password" id="password" class="form-control"
									placeholder="Password">
							</div>
							<div class="form-group">
									<button type="submit" class="btn btn-primary">ログイン</button>
							</div>
						</fieldset>
					</form:form>
				</div>
			</div>
		</div>

</body>
</html>