<#-- index.ftl -->
<#include "base.ftl">

<#macro title>Лента инициатив</#macro>

<#macro content>
    <script src="${contextPath}/static/js/date_utils.js"></script>

    <div class="container py-4">
        <div class="d-flex align-items-center justify-content-between mb-3">
            <h1 class="h3 text-primary m-0">Лента инициатив</h1>
            <a href="${contextPath}/new-initiative" class="btn btn-primary">Опубликовать инициативу</a>
        </div>

        <#if initiatives?? && initiatives?has_content>
            <div class="row g-3">
                <#list initiatives as initiative>
                    <div class="col-12">
                        <div class="card border-primary shadow-sm">
                            <div class="card-body">

                                <div class="d-flex align-items-start justify-content-between mb-3">
                                    <div>
                                        <h2 class="h5 text-primary mb-1">
                                            <a href="${contextPath}/initiative/${initiative.initiativeId}" class="link-primary text-decoration-none">
                                                ${initiative.title}
                                            </a>
                                            <div class="text-muted small">
                                                <span class="initiative-created-date">${initiative.createdAt!}</span>
                                            </div>
                                        </h2>
                                    </div>

                                    <#-- statuses -->
                                    <#assign status = initiative.status?string>
                                    <#assign statusClassMap = {
                                    "SUGGESTED":   "bg-warning text-dark",
                                    "APPROVED":    "bg-success",
                                    "REJECTED":    "bg-danger",
                                    "IN_PROGRESS": "bg-info text-dark",
                                    "COMPLETED":   "bg-secondary"
                                    }>
                                    <#assign statusClass = statusClassMap[status]!"bg-primary">
                                    <span class="badge ${statusClass}">${status}</span>
                                </div>

                                <#-- images -->
                                <#if initiative.images?? && initiative.images?size gt 0>
                                    <#assign carouselId = "initiativeCarousel-" + (initiative.initiativeId?string)>
                                    <div id="${carouselId}" class="carousel slide mb-3" data-bs-ride="carousel">
                                        <div class="carousel-inner">
                                            <#list initiative.images as image>
                                                <div class="carousel-item ${image?is_first?then('active','')}">
                                                    <a href="${contextPath}/initiative/${initiative.initiativeId}" class="d-block">
                                                        <img
                                                                src="${(image.url)!((image.path)!'')}"
                                                                class="d-block w-100"
                                                                alt="Изображение инициативы ${initiative.title!''}"
                                                                loading="lazy" width="1200" height="675">
                                                    </a>
                                                </div>
                                            </#list>
                                        </div>
                                        <#if initiative.images?size gt 1>
                                            <button class="carousel-control-prev" type="button" data-bs-target="#${carouselId}" data-bs-slide="prev">
                                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                                <span class="visually-hidden">Предыдущий</span>
                                            </button>
                                            <button class="carousel-control-next" type="button" data-bs-target="#${carouselId}" data-bs-slide="next">
                                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                                <span class="visually-hidden">Следующий</span>
                                            </button>
                                        </#if>
                                    </div>
                                </#if>

                                <#-- preview text: first 2 sentences -->
                                <#assign bodyText = (initiative.body!'')?trim>
                                <#if bodyText?has_content>
                                    <#assign parts = bodyText?split('. ')>
                                    <#assign first = (parts?size > 0)?then(parts[0],'')>
                                    <#assign second = (parts?size > 1)?then(parts[1],'')>
                                    <#assign preview = first + (second?has_content?then('. ' + second, ''))>
                                    <p class="card-text mb-3">
                                        ${preview}<#if bodyText?length gt preview?length>…</#if>
                                        <a href="${contextPath}/initiative/${initiative.initiativeId}" class="text-decoration-none">Читать далее</a>
                                    </p>
                                </#if>

                                <div class="d-flex flex-wrap align-items-center gap-2">
                                    <#-- Лайк -->
                                    <form action="${contextPath}/initiative/${initiative.initiativeId}/like" method="post" class="m-0">
                                        <#assign liked = (initiative.likedByMe?? && initiative.likedByMe)>
                                        <button type="submit"
                                                class="btn ${liked?then('btn-primary','btn-outline-primary')}"
                                                aria-pressed="${liked?c}">
                                            <span class="me-1">Лайк</span>
                                            <span class="badge bg-primary">
                                                <#if initiative.likes??>
                                                    <#if initiative.likes?is_sequence || initiative.likes?is_hash>
                                                        ${initiative.likes?size}
                                                    <#else>
                                                        ${initiative.likes}
                                                    </#if>
                                                <#else>0</#if>
                                            </span>
                                            <span class="visually-hidden">Поставить или убрать лайк</span>
                                        </button>
                                    </form>

                                    <a href="${contextPath}/initiative/${initiative.initiativeId}" class="btn btn-outline-secondary">
                                        Перейти к инициативе
                                    </a>
                                </div>

                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="alert alert-secondary m-0" role="alert">Пока нет инициатив.</div>
        </#if>
    </div>
</#macro>
