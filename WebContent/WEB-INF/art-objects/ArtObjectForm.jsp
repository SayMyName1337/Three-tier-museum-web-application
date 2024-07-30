<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Форма Произведения Искусства</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 50%;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            margin-top: 50px;
        }
        h1, h3 {
            color: #333;
        }
        .form-table {
            width: 100%;
            border-collapse: collapse;
        }
        .form-table th, .form-table td {
            padding: 10px;
            text-align: left;
        }
        .form-table th {
            width: 30%;
            background-color: #f4f4f9;
        }
        .form-table input[type="text"], 
        .form-table input[type="number"],
        .form-table input[type="file"],
        .form-table textarea {
            width: calc(100% - 20px);
            padding: 8px;
            margin: 5px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .form-table .buttons {
            text-align: center;
            padding-top: 20px;
        }
        .form-table .buttons input[type="submit"], .form-table .buttons a {
            display: inline-block;
            padding: 10px 20px;
            margin: 10px 5px;
            border: none;
            background-color: #5cb85c;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .form-table .buttons a {
            background-color: #d9534f;
        }
    </style>
</head>
<body>
<div class="container">
    <center>
        <h1>Форма для работы с произведениями искусства</h1>
        <h2>
            <c:if test="${museum != null}">
                <a href="/art-objects/?museum-id=<c:out value='${museum.id}'/>">
                    Список произведений искусства в музее «<c:out value="${museum.name}"/>»
                </a>
            </c:if>
            <c:if test="${museum == null}">
                <a href="/art-objects/">
                    Список произведений искусства
                </a>
            </c:if>
        </h2>
    </center>
    <div>
        <c:if test="${artObject != null}">
            <form action="update" method="post" enctype='multipart/form-data'>
        </c:if>
        <c:if test="${artObject == null}">
            <form action="insert" method="post" enctype='multipart/form-data'>
        </c:if>
            <table class="form-table">
                <caption>
                    <h3>
                        <c:if test="${artObject != null}">
                            Редактирование произведения искусства
                        </c:if>
                        <c:if test="${artObject == null}">
                            Добавление произведения искусства
                        </c:if>
                    </h3>
                </caption>
                <c:if test="${artObject != null}">
                    <input type="hidden" name="id" value="<c:out value='${artObject.id}'/>"/>
                </c:if>
                <tr>
                    <th>Название:</th>
                    <td>
                        <input type="text" name="name" size="45" value="<c:out value='${artObject.name}'/>"/>
                    </td>
                </tr>
                <tr>
                    <th>Описание:</th>
                    <td>
                        <textarea name="description" rows="5" cols="42"><c:out value='${artObject.description}'/></textarea>
                    </td>
                </tr>
                <tr>
                    <th>Год создания:</th>
                    <td>
                        <input type="number" name="creationYear" value="<c:out value='${artObject.creationYear}'/>"/>
                    </td>
                </tr>
                <tr>
                    <th>Художник:</th>
                    <td>
                        <input type="text" name="artist" value="<c:out value='${artObject.artist}'/>"/>
                    </td>
                </tr>
                <tr>
                    <th>Идентификатор музея:</th>
                    <td>
                        <c:if test="${museum != null}">
                            <input type="number" name="museumId" value="<c:out value='${museum.id}'/>"/>
                        </c:if>
                        <c:if test="${museum == null}">
                            <input type="number" name="museumId" value="<c:out value='${artObject.museumId}'/>"/>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <th>Фото:</th>
                    <td>
                        <input type="file" name="photo"/>
                    </td>
                </tr>
                <tr class="buttons">
                    <td colspan="2" class="buttons">
                        <input type="submit" value="Сохранить"/>
                        <a href="/art-objects/">Отменить</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>
