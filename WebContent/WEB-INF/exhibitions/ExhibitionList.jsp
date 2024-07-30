<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Музейное приложение</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <style>
        body {
            padding-top: 50px;
        }
        .navbar {
            margin-bottom: 20px;
        }
        .table th, .table td {
            text-align: center;
        }
        .btn {
            margin-top: 5px;
        }
        .actions {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="#">Музейное приложение</a>
</nav>

<div class="container">
    <h1 class="text-center">Управление выставками</h1>
    <div class="text-center mb-4">
        <a href="/museums/list" class="btn btn-primary">Список музеев</a>
        <c:if test="${museum != null}">
            <a href="/exhibitions/new?museum-id=<c:out value='${museum.id}'/>" class="btn btn-success">
                Добавить выставку в музей «<c:out value="${museum.name}"/>»
            </a>
        </c:if>
        <c:if test="${museum == null}">
            <a href="/exhibitions/new/" class="btn btn-success">
                Добавить выставку
            </a>
        </c:if>
        <!-- Кнопка для генерации отчета по выставкам -->
        <a href="/ExhibitionReportServlet" class="btn btn-info">Скачать отчет по выставкам</a>
    </div>
    
    <div class="table-responsive">
        <c:choose>
            <c:when test="${listExhibition.size() == 0}">
                <h3 class="text-center">Выставки в музее «<c:out value="${museum.name}"/>» не найдены</h3>
            </c:when>
            <c:otherwise>
                <table class="table table-striped table-bordered">
                    <caption><h2 class="text-center">Список выставок
                        <c:if test="${museum != null}">
                            в музее «<c:out value="${museum.name}"/>»
                        </c:if>
                    </h2></caption>
                    <thead class="thead-dark">
                        <tr>
                            <th>ID</th>
                            <th>Название</th>
                            <th>Описание</th>
                            <th>Начало</th>
                            <th>Конец</th>
                            <th>Id музея</th>
                            <th>Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="exhibition" items="${listExhibition}">
                            <tr>
                                <td><c:out value="${exhibition.id}"/></td>
                                <td><c:out value="${exhibition.name}"/></td>
                                <td><c:out value="${exhibition.description}"/></td>
                                <td><c:out value="${exhibition.started}"/></td>
                                <td><c:out value="${exhibition.ended}"/></td>
                                <td><c:out value="${exhibition.museumId}"/></td>
                                <td class="actions">
                                    <a href="/exhibitions/exhibited-ao?id=<c:out value='${exhibition.id}' />" class="btn btn-info btn-sm">Произведения искусства</a>
                                    <a href="/exhibitions/select-ao-to-attach?ex-id=${exhibition.id}" class="btn btn-secondary btn-sm">Привязать произведение искусства</a>
                                    <a href="/exhibitions/edit?id=<c:out value='${exhibition.id}' />" class="btn btn-warning btn-sm">Изменить</a>
                                    <a href="/exhibitions/delete?id=<c:out value='${exhibition.id}' />" class="btn btn-danger btn-sm">Удалить</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
