<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
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
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/museums/new">Добавить музей</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/exhibitions">Список всех выставок</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/art-objects/">Список всех произведений искусства</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/ExportToExcelServlet">Экспортировать в Excel</a>
                </li>
            </ul>
        </div>
    </nav>
    
    <div class="container">
        <h1 class="text-center">Управление музеями</h1>
        
        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <caption><h2 class="text-center">Список музеев</h2></caption>
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Описание</th>
                        <th>Адрес</th>
                        <th>Время открытия</th>
                        <th>Время закрытия</th>
                        <th>Всего выставок</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="museum" items="${listMuseum}">
                        <tr>
                            <td><c:out value="${museum.id}" /></td>
                            <td><c:out value="${museum.name}" /></td>
                            <td><c:out value="${museum.description}" /></td>
                            <td><c:out value="${museum.address}" /></td>
                            <td>${fn:substring( museum.timeOpening, 0, 5 )}</td>
                            <td>${fn:substring( museum.timeClosing, 0, 5 )}</td>
                            <td><c:out value="${museum.exhibitionCount}" /></td>
                            <td class="actions">
                                <a href="/exhibitions?museum-id=${museum.id}" class="btn btn-info btn-sm">Выставки</a>
                                <a href="/art-objects?museum-id=${museum.id}" class="btn btn-info btn-sm">Произведения искусства</a>
                                <a href="/museums/edit?id=${museum.id}" class="btn btn-warning btn-sm">Изменить</a>
                                <a href="/museums/delete?id=${museum.id}" class="btn btn-danger btn-sm">Удалить</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="text-center">
            <a href="/ReportServlet" class="btn btn-primary mt-3">Скачать отчет</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
