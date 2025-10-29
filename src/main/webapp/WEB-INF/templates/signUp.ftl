<#-- signUp.ftl -->
<#include "base.ftl">

<#macro title>SignUp page</#macro>

<#macro content>
    <div class="register-card">
        <h2 class="register-title">Регистрация</h2>
        <form action="/signUp" method="post">
            <div class="input-wrapper">
                <label for="name">Имя</label>
                <input type="text" id="name" name="name" class="input-blue" required>
            </div>
            <div class="input-wrapper">
                <label for="login">Логин</label>
                <input type="text" id="login-input" name="login" class="input-blue" required>
            </div>
            <div class="input-wrapper">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" class="input-blue" required>
            </div>
            <button type="submit" id="submit-btn" class="submit-btn">Зарегистрироваться</button>
            <#if error?? && error?has_content>
                <div id="login-error" class="error-message">${error}</div>
            </#if>
        </form>
    </div>

<#-- check that login don't exist-->
    <script>
        const submit = document.getElementById('submit-btn');
        $('#login-input').on('input', function () {
            submit.disabled = false;
            const value = this.value;
            $.get('/user/check', {login: value}, function (response) {
                $('#login-error').text(response);

                // disable submit button
                if (response !== "") {
                    submit.disabled = true;
                }
            });
        })
    </script>
</#macro>
