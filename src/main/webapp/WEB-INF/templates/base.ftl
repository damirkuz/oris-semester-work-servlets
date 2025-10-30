<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><@title/></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="/static/style.css">

    <!-- Bootstrap 5 JS bundle-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>

<#-- header -->
<#if showHeader?? && showHeader == false>
<#-- header is hide -->
<#else>
    <header class="site-header">
        <div class="header-container">
            <div class="header-left">
                <a href="${contextPath}/" class="header-logo">Главная</a>
            </div>

            <div class="header-right">
                <#-- by default — buttons login and signUp -->
                <#if userLoggedIn?? && userLoggedIn>
                    <a href="${contextPath}/profile/${userLogin}" class="header-btn">Личный кабинет</a>
                    <a href="${contextPath}/logout" class="header-btn header-btn-outline">Выйти</a>
                <#else>
                    <a href="${contextPath}/login" class="header-btn">Вход</a>
                    <a href="${contextPath}/signUp" class="header-btn header-btn-outline">Регистрация</a>
                </#if>
            </div>
        </div>
    </header>
</#if>

<script>
    // format initiative created date with user timeZone
    document.addEventListener("DOMContentLoaded", function() {
        document.querySelectorAll('.initiative-created-date').forEach(el => {
            const raw = el.textContent?.trim();
            if (!raw) return;
            const d = new Date(raw);
            if (isNaN(d)) return;
            el.textContent = d.toLocaleString('ru-RU', {
                year: 'numeric', month: '2-digit', day: '2-digit',
                hour: '2-digit', minute: '2-digit', second: '2-digit'
            });
        });
    });
</script>

<div id="content">
    <div class="content">
        <@content/>
    </div>
</div>
</body>
</html>
