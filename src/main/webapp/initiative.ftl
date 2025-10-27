<#include "base.ftl">

<#macro title>Инициатива</#macro>

<#macro content>
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-12 col-lg-10">
                <div class="card border-primary shadow-sm">
                    <div class="card-body">

                        <script>
                            // format initiative created date with user timeZone
                            document.addEventListener("DOMContentLoaded", function() {
                                const registrationDateElem = document.getElementById('initiative-created-date');
                                const rawDateStr = registrationDateElem.textContent;
                                if (rawDateStr) {
                                    const date = new Date(rawDateStr);
                                    const formattedDate = date.toLocaleString('ru-RU', {
                                        year: 'numeric',
                                        month: '2-digit',
                                        day: '2-digit',
                                        hour: '2-digit',
                                        minute: '2-digit',
                                        second: '2-digit'
                                    });
                                    registrationDateElem.textContent = formattedDate;
                                }
                            });
                        </script>


                        <div class="d-flex align-items-start justify-content-between mb-3">
                            <div>
                                <h1 class="h3 text-primary mb-1">${initiative.title}</h1>
                                <div class="text-muted small">
                                    <span id="initiative-created-date">${initiative.createdAt!}</span>
                                </div>
                            </div>
                            <#-- Бейдж статуса -->
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

                        <#-- Карусель изображений -->
                        <#if initiative.images?? && initiative.images?size gt 0>
                            <#assign carouselId = "initiativeCarousel-" + (initiative.initiativeId?string)>
                            <div id="${carouselId}" class="carousel slide mb-4" data-bs-ride="carousel">
                                <div class="carousel-inner">
                                    <#list initiative.images as image>
                                        <div class="carousel-item ${image?is_first?then('active','')}">
                                            <img
                                                    src="${(image.url)!((image.path)!'')}"
                                                    class="d-block w-100"
                                                    alt="Изображение инициативы ${initiative.title!''}"
                                                    loading="lazy" width="1200" height="675">
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


                        <p class="card-text mb-4">${initiative.body}</p>

                        <div class="d-flex flex-wrap align-items-center gap-2 mb-4">
                            <form action="/initiative/${initiative.initiativeId}/like" method="post" class="m-0">
                                <input type="hidden" name="_csrf" value="${_csrfToken!}">
                                <button type="submit"
                                        class="btn ${likedByMe?then('btn-primary','btn-outline-primary')}"
                                        aria-pressed="${likedByMe?c}">
                                    <span class="me-1">Лайк</span>
                                    <span class="badge bg-primary">${initiative.likes?size}</span>
                                    <span class="visually-hidden">Поставить или убрать лайк</span>
                                </button>
                            </form>

                            <#if canEditInitiative?? && canEditInitiative>
                                <a href="/initiative/${initiative.initiativeId}/edit" class="btn btn-outline-secondary">
                                    Редактировать инициативу
                                </a>
                            </#if>
                        </div>

                        <section aria-labelledby="comments-title">
                            <h2 id="comments-title" class="h5 text-primary mb-3">Комментарии</h2>

                            <#if initiative.comments?? && initiative.comments?has_content>
                                <ul class="list-group mb-3">
                                    <#list initiative.comments as comment>
                                        <li class="list-group-item">
                                            <div class="d-flex justify-content-between">
                                                <div>
                                                    <strong class="me-2">${comment.author}</strong>
                                                    <span>${comment.text}</span>
                                                </div>
                                                <div class="d-flex gap-2">
                                                    <#if comment.ownedByMe?? && comment.ownedByMe>
                                                        <button type="button" class="btn btn-sm btn-outline-primary"
                                                                data-edit-target="#edit-form-${comment?index}">
                                                            Изменить
                                                        </button>
                                                    </#if>
                                                    <#if isAdmin?? && isAdmin>
                                                        <form action="/comment/${comment.id}/delete" method="post" class="m-0">
                                                            <input type="hidden" name="_csrf" value="${_csrfToken!}">
                                                            <button type="submit" class="btn btn-sm btn-outline-danger">Удалить</button>
                                                        </form>
                                                    </#if>
                                                </div>
                                            </div>

                                            <#if comment.ownedByMe?? && comment.ownedByMe>
                                                <form id="edit-form-${comment?index}" action="/comment/${comment.id}/edit" method="post"
                                                      class="row g-2 mt-2 d-none">
                                                    <input type="hidden" name="_csrf" value="${_csrfToken!}">
                                                    <div class="col-12 col-md">
                                                        <input type="text" name="text" value="${comment.text}" class="form-control" required>
                                                    </div>
                                                    <div class="col-12 col-md-auto">
                                                        <button type="submit" class="btn btn-primary">Сохранить</button>
                                                    </div>
                                                </form>
                                            </#if>
                                        </li>
                                    </#list>
                                </ul>
                            <#else>
                                <div class="alert alert-secondary" role="alert">Пока нет комментариев</div>
                            </#if>

                            <form action="/initiative/${initiative.initiativeId}/comment" method="post" class="row g-2">
                                <input type="hidden" name="_csrf" value="${_csrfToken!}">
                                <div class="col-12 col-md">
                                    <input type="text" name="comment" class="form-control" placeholder="Оставить комментарий..." required>
                                </div>
                                <div class="col-12 col-md-auto">
                                    <button type="submit" class="btn btn-primary w-100">Отправить</button>
                                </div>
                            </form>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Тоггл инлайн-редактирования комментария
        document.addEventListener('click', function(e) {
            const btn = e.target.closest('[data-edit-target]');
            if (!btn) return;
            const sel = btn.getAttribute('data-edit-target');
            const form = document.querySelector(sel);
            if (form) form.classList.toggle('d-none');
        });
    </script>
</#macro>
