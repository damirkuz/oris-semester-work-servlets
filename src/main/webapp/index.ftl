<#include "base.ftl">

<#macro title>Лента инициатив</#macro>

<#macro content>
    <div class="feed-container">
        <header class="feed-header">
            <h1 class="feed-title">Лента инициатив</h1>
            <a href="/initiative/new" class="new-initiative-btn">Опубликовать инициативу</a>
        </header>

        <#if initiatives?? && initiatives?has_content>
            <div class="initiatives-list">
                <#list initiatives as initiative>
                    <article class="initiative-card">
                        <h2 class="initiative-title">${initiative.title}</h2>
                        <p class="initiative-description">${initiative.description}</p>

                        <#if initiative.photoUrl?? && initiative.photoUrl?has_content>
                            <img src="${initiative.photoUrl}" alt="Фото инициативы" class="initiative-photo"/>
                        </#if>

                        <div class="initiative-actions">
                            <form action="/initiative/${initiative.id}/like" method="post">
                                <button type="submit" class="like-btn">Лайк (${initiative.likes})</button>
                            </form>

                            <#if initiative.canEdit?? && initiative.canEdit>
                                <a href="/initiative/${initiative.id}/edit" class="edit-btn">Редактировать</a>
                            </#if>
                        </div>

                        <section class="comments">
                            <h3 class="comments-title">Комментарии</h3>

                            <#if initiative.comments?? && initiative.comments?has_content>
                                <#list initiative.comments as comment>
                                    <div class="comment">
                                        <b>${comment.author}</b>: ${comment.text}
                                    </div>
                                </#list>
                            <#else>
                                <div class="comment comment-empty">Пока нет комментариев</div>
                            </#if>

                            <form action="/initiative/${initiative.id}/comment" method="post" class="comment-form">
                                <input type="text" name="comment" class="comment-input" placeholder="Оставить комментарий..." required/>
                                <button type="submit" class="comment-btn">Отправить</button>
                            </form>
                        </section>
                    </article>
                </#list>
            </div>
        <#else>
            <p class="empty-feed">Пока нет инициатив.</p>
        </#if>
    </div>
</#macro>
