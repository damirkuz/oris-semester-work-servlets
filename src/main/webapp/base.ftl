<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><@title/></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="/static/style.css">

    <!-- Bootstrap 5 JS bundle-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

<#-- header -->
<#if showHeader?? && showHeader == false>
<#-- header is hide -->
<#else>
    <header class="site-header">
        <div class="header-container">
            <div class="header-left">
                <a href="/" class="header-logo">Главная</a>
            </div>

            <div class="header-right">
                <#-- by default — buttons login and signUp -->
                <#if userLoggedIn?? && userLoggedIn>
                    <a href="/profile/${userLogin}" class="header-btn">Личный кабинет</a>
                    <a href="/logout" class="header-btn header-btn-outline">Выйти</a>
                <#else>
                    <a href="/login" class="header-btn">Вход</a>
                    <a href="/signUp" class="header-btn header-btn-outline">Регистрация</a>
                </#if>
            </div>
        </div>
    </header>
</#if>

<div id="content">
    <div class="content">
        <@content/>
    </div>
</div>
</body>
</html>
