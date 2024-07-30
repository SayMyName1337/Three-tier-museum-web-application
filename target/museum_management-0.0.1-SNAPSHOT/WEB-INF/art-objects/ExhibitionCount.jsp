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
        .container {
            text-align: center;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="#">Музейное приложение</a>
</nav>
<div class="container">
    <h1 class="mt-5">Управление произведениями искусства</h1>
    <h2 class="my-4">
        <a href="/museums/list" class="btn btn-primary">Список музеев</a>
    </h2>
    <div class="my-4">
        <h2>Количество выставок, в котором принимало участие произведение искусства "<c:out value='${artObject.name}'/>": ${exhibitionsCount}</h2>
    </div>
    <a href="javascript:history.back()" class="btn btn-secondary">Назад</a>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
