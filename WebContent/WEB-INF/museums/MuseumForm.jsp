<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Музейное приложение</title>
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
        .form-table input[type="time"],
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
        <h1>Управление музеями</h1>
    </center>
    <div>
        <c:if test="${museum != null}">
            <form action="update" method="post">
        </c:if>
        <c:if test="${museum == null}">
            <form action="insert" method="post">
        </c:if>
                <table class="form-table">
                    <caption>
                        <h3>
                            <c:if test="${museum != null}">
                                Редактирование данных музея
                            </c:if>
                            <c:if test="${museum == null}">
                                Добавить музей
                            </c:if>
                        </h3>
                    </caption>
                    <c:if test="${museum != null}">
                        <input type="hidden" name="id" value="<c:out value='${museum.id}'/>"/>
                    </c:if>
                    <tr>
                        <th>Название:</th>
                        <td>
                            <input type="text" name="name" size="45" value="<c:out value='${museum.name}'/>"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Описание:</th>
                        <td>
                            <textarea name="description" rows="5" cols="42"><c:out value='${museum.description}'/></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th>Адрес:</th>
                        <td>
                            <input type="text" name="address" size="45" value="<c:out value='${museum.address}'/>"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Время открытия:</th>
                        <td>
                            <input required step="60" type="time" name="time_opened" value="<c:out value='${museum.timeOpening}'/>"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Время закрытия:</th>
                        <td>
                            <input required step="60" type="time" name="time_closed" value="<c:out value='${museum.timeClosing}'/>"/>
                        </td>
                    </tr>
                    <tr class="buttons">
                        <td colspan="2" class="buttons">
                            <input type="submit" value="Сохранить"/>
                            <a href="/museums/list">Отменить</a>
                        </td>
                    </tr>
                </table>
            </form>
    </div>
</div>
</body>
</html>
