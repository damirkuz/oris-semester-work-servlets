<#include "base.ftl">

<#macro title>Страница не найдена (404)</#macro>

<#macro content>
    <div class="error-page-container">
        <h1 class="error-code">404</h1>
        <h2 class="error-message">Упс! Страница не найдена</h2>
        <p class="error-description">Страница, которую вы ищете, не существует или была удалена.</p>
        <a href="${contextPath}/" class="btn-back-home">Вернуться на главную</a>
    </div>
</#macro>
