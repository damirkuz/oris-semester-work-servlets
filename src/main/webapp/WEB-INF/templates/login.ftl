<#-- login.ftl -->
<#include "base.ftl">

<#macro title>Login page</#macro>

<#macro content>
    <div class="login-card">
        <h2 class="login-title">Вход</h2>
        <form action="${contextPath}/login" method="post">
            <div class="input-wrapper">
                <label for="login">Логин</label>
                <input type="text" id="login" name="login" class="input-blue" required>
            </div>

            <div class="input-wrapper">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" class="input-blue" required>
            </div>

            <button type="submit" class="submit-btn">Войти</button>
            <#if error?? && error?has_content>
                <div id="login-error" class="error-message">${error}</div>
            </#if>
        </form>
    </div>
</#macro>

</html>
