<#include "base.ftl">

<#macro title>Новая инициатива</#macro>

<#macro content>
    <#assign maxCount = (maxCount)!10>
    <#assign maxSizeMb = (maxSizeMb)!20>

    <div class="new-initiative-container">
        <header class="page-header">
            <h1 class="page-title">Опубликовать инициативу</h1>
            <a href="${contextPath}/" class="back-link">← Вернуться к ленте</a>
        </header>

        <#-- Показ ошибок сервера -->
        <#if errors?? && errors?has_content>
            <div class="alert alert-error">
                <ul class="alert-list">
                    <#list errors as e>
                        <li>${e}</li>
                    </#list>
                </ul>
            </div>
        <#elseif error?? && error?has_content>
            <div class="alert alert-error">${error}</div>
        </#if>

        <form id="initiativeForm"
              class="initiative-form"
              action="${contextPath}/new-initiative"
              method="post"
              enctype="multipart/form-data"
              novalidate>

            <div class="form-group">
                <label for="title">Заголовок</label>
                <input id="title"
                       name="title"
                       type="text"
                       maxlength="120"
                       required
                       placeholder="Коротко и по делу (до 120 символов)"
                       value="${(form.title)!''}"/>
                <small class="hint">Обязательное поле. От 5 до 120 символов.</small>
            </div>

            <div class="form-group">
                <label for="description">Описание</label>
                <textarea id="description"
                          name="description"
                          rows="6"
                          maxlength="2000"
                          required
                          placeholder="Опишите проблему и предлагаемые решения">${(form.description)!''}</textarea>
                <small class="hint">Обязательное поле. До 2000 символов.</small>
            </div>

            <div class="mb-3">
                <label for="photos" class="form-label">Добавить фото (необязательно)</label>
                <input id="photos"
                       name="photos[]"
                       type="file"
                       class="form-control"
                       accept="image/*"
                       multiple>
                <div class="form-text">До ${maxCount} изображений, максимум ${maxSizeMb} МБ каждое (JPEG, PNG, WEBP).</div>

                <div id="imagesPreview" class="mt-2" style="display:none;">
                    <div class="row row-cols-2 row-cols-md-6 g-2" id="imagesPreviewGrid"></div>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn primary">Опубликовать</button>
                <a href="${contextPath}/" class="btn outline">Отмена</a>
            </div>
        </form>

        <#if (RequestParameters.error??)>
            <div class="error-message">
                <#if RequestParameters.error == "invalidImageExtension">Неккоректное расширение</#if>
                <#if RequestParameters.error == "invalidImageSize">Изображение больше лимита по весу</#if>
                <#if RequestParameters.error == "invalidImageName">Некорректное имя изображения</#if>
                <#if RequestParameters.error == "invalidInitiativeTitle">Некорректный заголовок инициативы</#if>
                <#if RequestParameters.error == "failInitiativeSave">Ошибка сохранения инициативы</#if>
            </div>
        </#if>

    </div>
</#macro>
