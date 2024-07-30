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
        .content {
            text-align: center;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <a class="navbar-brand" href="#">Музейное приложение</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/museums/list">Список музеев</a>
                </li>
            </ul>
        </div>
    </nav>
    
    <div class="container content">
        <h1 class="text-center">Управление произведениями искусства</h1>
        <h3 class="text-center">Выставки, в которых принимало участие произведение искусства "<c:out value='${artObject.name}'/>"</h3>
        <c:choose>
            <c:when test="${listExhibitionTakedPlace.size() == 0}">
                <h3 class="text-center">Выставки, в которых принимало участие произведение искусства "<c:out value='${artObject.name}'/>" не найдены</h3>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
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
                            <c:forEach var="exhibition" items="${listExhibitionTakedPlace}">
                                <tr>
                                    <td><c:out value="${exhibition.id}"/></td>
                                    <td><c:out value="${exhibition.name}"/></td>
                                    <td><c:out value="${exhibition.description}"/></td>
                                    <td><c:out value="${exhibition.started}"/></td>
                                    <td><c:out value="${exhibition.ended}"/></td>
                                    <td><c:out value="${exhibition.museumId}"/></td>
                                    <td>
                                        <a href="/exhibitions/remove-ao-from-ex?ex-id=${exhibition.id}&ao-id=<c:out value='${artObject.id}' />" class="btn btn-danger btn-sm">
                                            Убрать с выставки
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="text-center mt-4">
            <a href="javascript:history.back()" class="btn btn-secondary">Назад</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
