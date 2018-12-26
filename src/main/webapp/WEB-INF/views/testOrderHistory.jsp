<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>注文履歴</title>
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/piza.css" rel="stylesheet">
</head>
<body>

		<!-- table -->
		<h3 class="text-center">注文内容確認</h3>
		<div class="row">
			<div
				class="table-responsive col-lg-offset-1 col-lg-10 col-md-offset-1 col-md-10 col-sm-10 col-xs-12">
					
				<table class="table table-striped table table-bordered">
					<tbody>
						<tr>
							<td>
								商品ID
							</td>
							<td>
								商品合計
							</td>

						</tr>
						<c:forEach var="order" items="${orderList}">
						<tr>
							<td>
							<c:out value="${order.id}"/>
							</td>
							<td>
							<c:out value="${order.totalPrice}"/>
							</td>
							
						</tr>
							</c:forEach>
					</tbody>
				</table>

			</div>
		</div>

	</div>
</body>
</html>