<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Музейное приложение</title>
</head>
<body>
	<center>
		<h1>Управление выставками</h1>
        <h2>
        	<a href="new">Добавить выставку</a>
        	&nbsp;&nbsp;&nbsp;
        	<a href="list">Список выставок</a>
            &nbsp;&nbsp;&nbsp;
        </h2>
	</center>
    <div align="center">
        <table border="1" cellpadding="5">
            <caption><h2>Список выставок</h2></caption>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Описание</th>
                <th>Адрес</th>
                <th>Время открытия</th>
                <th>Время закрытия</th>
                <th>Действия</th>
            </tr>
            <c:forEach var="museum" items="${listMuseum}">
                <tr>
                    <td><c:out value="${museum.id}" /></td>
                    <td><c:out value="${museum.name}" /></td>
                    <td><c:out value="${museum.description}" /></td>
                    <td><c:out value="${museum.address}" /></td>
                    <td><c:out value="${museum.timeOpening}" /></td>
                    <td><c:out value="${museum.timeClosing}" /></td>
                    <td>
                    	<a href="edit?id=<c:out value='${museum.id}' />">Изменить</a>
                    	<br>
                    	<a href="delete?id=<c:out value='${museum.id}' />">Удалить</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>	
</body>
</html>
