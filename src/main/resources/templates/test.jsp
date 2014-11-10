<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Title</title>
</head>
<body>
	<form:form>
		<table>
			<tr>
				<td>Firstname:</td>
				<td><form:input path="firstname"/></td>
			</tr>
			<tr>
				<td>Lastname:</td>
				<td><form:input path="lastname"/></td>
			</tr>
			<tr>
				<td><input type="submit" value="savechanges"></td>
				<td>
			</tr>
		</table>
	</form:form>
</body>
</html>