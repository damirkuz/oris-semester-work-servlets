<#-- base.ftl -->
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><@title/></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="${contextPath}/static/style.css">

    <!-- Bootstrap 5 JS bundle-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>

<#include "partials/header.ftl">

<div id="content">
    <div class="content">
        <@content/>
    </div>
</div>
</body>
</html>
