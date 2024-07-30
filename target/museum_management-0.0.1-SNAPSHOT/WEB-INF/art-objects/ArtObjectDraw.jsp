<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Рисование на изображении</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <style>
        body {
            padding-top: 50px;
        }
        .container {
            text-align: center;
        }
        canvas {
            border: 1px solid black;
            margin-top: 20px;
        }
        .btn {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="#">Музейное приложение</a>
</nav>
<div class="container">
    <h1>Рисование на изображении</h1>
    <div style="position: relative;">
        <canvas id="artCanvas" width="350" height="350"></canvas>
    </div>
    <button type="button" class="btn btn-success" onclick="saveDrawing()">Сохранить изменения</button>
</div>

<script>
    var canvas = document.getElementById('artCanvas');
    var ctx = canvas.getContext('2d');
    var painting = false;

    var img = new Image();
    img.src = 'data:image/jpeg;base64,${artObject.photoBase64}';
    img.onload = function() {
        ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
    };

    function startPosition(e) {
        painting = true;
        draw(e);
    }

    function endPosition() {
        painting = false;
        ctx.beginPath();
    }

    function draw(e) {
        if (!painting) return;

        ctx.lineWidth = 5;
        ctx.lineCap = 'round';
        ctx.strokeStyle = 'red';

        var rect = canvas.getBoundingClientRect();
        var x = e.clientX - rect.left;
        var y = e.clientY - rect.top;

        ctx.lineTo(x, y);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(x, y);
    }

    canvas.addEventListener('mousedown', startPosition);
    canvas.addEventListener('mouseup', endPosition);
    canvas.addEventListener('mousemove', draw);

    function saveDrawing() {
        var drawingDataURL = canvas.toDataURL('image/jpeg');
        var form = document.createElement('form');
        form.method = 'post';
        form.action = '<%= request.getContextPath() %>/art-objects/draw';
        
        var inputId = document.createElement('input');
        inputId.type = 'hidden';
        inputId.name = 'id';
        inputId.value = '${artObject.id}';
        form.appendChild(inputId);

        var inputDrawing = document.createElement('input');
        inputDrawing.type = 'hidden';
        inputDrawing.name = 'drawing';
        inputDrawing.value = drawingDataURL;
        form.appendChild(inputDrawing);

        document.body.appendChild(form);
        form.submit();
    }
</script>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
