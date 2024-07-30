<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Привязка произведения искусства к выставке</title>
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
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/museums/list">Список музеев</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/exhibitions/listlist?museum-id=<c:out value='${museum.id}'/>">
                        Список выставок в музее «<c:out value="${museum.name}"/>»
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/art-objects/new?museum-id=<c:out value='${museum.id}'/>">
                        Добавить произведение искусства в музей «<c:out value="${museum.name}"/>»
                    </a>
                </li>
            </ul>
        </div>
    </nav>
    
    <div class="container">
        <h1 class="text-center">Привязка произведения искусства к выставке «<c:out value="${exhibition.name}"/>»</h1>
        <div class="table-responsive">
            <c:choose>
                <c:when test="${empty artObjects}">
                    <h3 class="text-center">Произведения искусства в музее "<c:out value='${museum.name}'/>", которые еще не привязаны к выставке «<c:out value="${exhibition.name}"/>», не найдены</h3>
                </c:when>
                <c:otherwise>
                    <table class="table table-striped table-bordered">
                        <caption><h2 class="text-center">Список произведений искусства в музее "<c:out value='${museum.name}'/>", которые еще не привязаны к выставке «<c:out value="${exhibition.name}"/>»</h2></caption>
                        <thead class="thead-dark">
                            <tr>
                                <th>ID</th>
                                <th>Название</th>
                                <th>Описание</th>
                                <th>Год создания</th>
                                <th>Id музея</th>
                                <th>Фото</th>
                                <th>Действия</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="artObject" items="${artObjects}">
                                <tr>
                                    <td><c:out value="${artObject.id}"/></td>
                                    <td><c:out value="${artObject.name}"/></td>
                                    <td><c:out value="${artObject.description}"/></td>
                                    <td><c:out value="${artObject.creationYear}"/></td>
                                    <td><c:out value="${artObject.museumId}"/></td>
                                    <td>
                                        <c:if test="${not empty artObject.photo}">
                                            <img width="150" src="data:image/jpeg;base64,${artObject.photoBase64}"/>
                                        </c:if>
                                    </td>
                                    <td class="actions">
                                        <a href="/exhibitions/attach-to-exhibition?ex-id=${exhibition.id}&ao-id=<c:out value='${artObject.id}' />" class="btn btn-primary btn-sm">
                                            Привязать к выставке «<c:out value="${exhibition.name}"/>»
                                        </a>
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
