<#-- edit_initiative.ftl -->
<#include "base.ftl">

<#macro title>Редактировать инициативу</#macro>

<#macro content>
    <#assign maxCount = (maxCount)!10>
    <#assign maxSizeMb = (maxSizeMb)!20>

    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-12 col-lg-10">
                <div class="card shadow-sm border-0">
                    <div class="card-body">

                        <div class="d-flex align-items-center justify-content-between mb-3">
                            <h1 class="h4 mb-0">Редактировать инициативу</h1>
                            <a href="${contextPath}/initiative/${initiative.initiativeId}" class="btn btn-outline-secondary">Назад</a>
                        </div>

                        <#-- Серверные ошибки -->
                        <#if errors?? && errors?has_content>
                            <div class="alert alert-danger" role="alert">
                                <ul class="mb-0">
                                    <#list errors as e>
                                        <li>${e}</li>
                                    </#list>
                                </ul>
                            </div>
                        <#elseif error?? && error?has_content>
                            <div class="alert alert-danger" role="alert">${error}</div>
                        </#if>

                        <form id="initiativeEditForm"
                              action="${contextPath}/initiative/${initiative.initiativeId}/edit"
                              method="post"
                              enctype="multipart/form-data"
                              novalidate>

                            <div class="mb-3">
                                <label for="title" class="form-label">Заголовок</label>
                                <input id="title"
                                       name="title"
                                       type="text"
                                       maxlength="120"
                                       required
                                       class="form-control"
                                       placeholder="Коротко и по делу (до 120 символов)"
                                       value="${(form.title)!initiative.title!''}">
                                <div class="form-text">Обязательное поле. От 5 до 120 символов.</div>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Описание</label>
                                <textarea id="description"
                                          name="description"
                                          rows="6"
                                          maxlength="2000"
                                          required
                                          class="form-control"
                                          placeholder="Опишите изменения и уточнения">${(form.description)!initiative.body!''}</textarea>
                                <div class="form-text">Обязательное поле. До 2000 символов.</div>
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

                            <#-- Опциональное редактирование статуса -->
                            <#if isAdmin?? && isAdmin>
                                <div class="mb-3">
                                    <label for="status" class="form-label">Статус</label>
                                    <#assign currentStatus = (form.status)!((initiative.status)?string)!'SUGGESTED'>
                                    <select id="status" name="status" class="form-select">
                                        <#list initiativeStatuses as s>
                                            <option value="${s}" <#if s == currentStatus>selected</#if>>${s}</option>
                                        </#list>
                                    </select>
                                    <div class="form-text">Обновите статус инициативы при необходимости.</div>
                                </div>
                            </#if>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary">Сохранить изменения</button>
                                <a href="${contextPath}/initiative/${initiative.initiativeId}" class="btn btn-outline-secondary">Отмена</a>
                            </div>
                        </form>


                        <#if (RequestParameters.error??)>
                            <div class="alert alert-warning mt-3" role="alert">
                                <#if RequestParameters.error == "invalidImageExtension">Некорректное расширение</#if>
                                <#if RequestParameters.error == "invalidImageSize">Изображение больше лимита по весу</#if>
                                <#if RequestParameters.error == "invalidImageName">Некорректное имя изображения</#if>
                                <#if RequestParameters.error == "invalidInitiativeTitle">Некорректный заголовок инициативы</#if>
                                <#if RequestParameters.error == "failInitiativeSave">Ошибка сохранения инициативы</#if>
                            </div>
                        </#if>

                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>
