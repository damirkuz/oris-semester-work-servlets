<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><@title/></title>

    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <link rel="stylesheet" href="/static/style.css">
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
                    <a href="/profile" class="header-btn">Личный кабинет</a>
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
