<#include "base.ftl">

<#macro title>Личный кабинет</#macro>

<#macro content>
    <div class="profile-container">

        <h1 class="profile-title">Личный кабинет</h1>

        <div class="profile-header">
            <div class="profile-avatar">
                <#if userProfile.profilePicture?? && userProfile.profilePicture?has_content>
                    <img src="${userProfile.profilePicture}" alt="Аватар пользователя" class="avatar-img" />
                <#else>
                    <img src="/static/images/default-avatar.png" alt="Стандартный аватар" class="avatar-img" />
                </#if>
            </div>

            <div class="profile-info">
                <p><strong>Логин:</strong> ${userProfile.login}</p>
                <p><strong>Имя:</strong> ${userProfile.name}</p>
            </div>
        </div>

        <#if isSelfUserProfile?? && isSelfUserProfile>
            <div class="profile-edit-form">
                <h2>Редактировать профиль</h2>
                <form action="${contextPath}/profile/${userProfile.login}" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="name">Новое имя</label>
                        <input type="text" id="name" name="name" class="input-blue" value="${userProfile.name!}" required />
                    </div>

                    <div class="form-group">
                        <label for="password">Новый пароль</label>
                        <input type="password" id="password" name="password" class="input-blue" />
                        <small>Оставьте поле пустым, если не хотите менять пароль</small>
                    </div>

                    <div class="mb-3">
                        <label for="avatar" class="form-label">Загрузить новый аватар</label>
                        <input class="form-control" type="file" id="avatar" name="avatar" accept="image/*">
                        <div class="form-text">Поддерживаются PNG, JPG, WEBP</div>
                    </div>

                    <#if error?? && error?has_content>
                        <div id="login-error" class="error-message">${error}</div>
                    </#if>

                    <button type="submit" class="submit-btn">Сохранить изменения</button>
                </form>
            </div>

            <#if (RequestParameters.error??)>
                <div class="error-message">
                    <#if RequestParameters.error == "invalidPassword">Пароль не соответствует требованиям</#if>
                    <#if RequestParameters.error == "noChanges">Изменений не обнаружено</#if>
                </div>
            </#if>

            <#if (RequestParameters.success??)>
                <div class="success-message">Профиль успешно обновлен</div>
            </#if>

        </#if>
    </div>
</#macro>
