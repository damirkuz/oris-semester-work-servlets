<#-- header.ftl -->
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