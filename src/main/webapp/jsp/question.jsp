<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
         by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="page_title" value="${question.title}" scope="request" />
<jsp:include page="layout/header.jsp" />
<style>
    .note-editor {
        max-width: 690px !important;
    }
    .alert {
        margin-bottom: 35px;
    }
</style>
    <div class="dx-blog-post dx-ticket dx-ticket-open">
        <div class="dx-separator"></div>
        <div style="background-color: #fafafa;">
            <ul class="dx-blog-post-info dx-blog-post-info-style-2 mb-0 mt-0">
                <li><span><span class="dx-blog-post-info-title"><fmt:message key="label.category" /></span>${question.category.name}</span></li>
                <li><span><span class="dx-blog-post-info-title"><fmt:message key="label.status" /></span>
                    <label class="form-check-label" for="status">
                        <fmt:message key="label.status.${question.closed ? 'closed' : 'open'}" />
                    </label>
                <label class="switch" style="margin-top: -5px;">
                    <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="status" id="status" onchange="questions.changeStatus('${pageContext.request.contextPath}', this, ${question.id});" ${question.closed ? 'checked' : ''}>
                    <span class="slider slider-light round"></span>
                </label>
                </span></li>
                <li><span><span class="dx-blog-post-info-title"><fmt:message key="label.modified" /></span>
                    <fmt:parseDate value="${!empty question.editingDate ? question.editingDate : question.creationDate}" pattern="y-M-dd'T'H:m:s" var="parsedDate" />
                    <fmt:formatDate value="${parsedDate}"  pattern="dd.MM.yyyy HH:mm:ss" />
                    (<at:time-duration date="${!empty question.editingDate ? question.editingDate : question.creationDate}"/>)
                </span></li>
            </ul>
        </div>
        <div class="dx-separator"></div>
        <div class="dx-comment dx-ticket-comment main-post">
            <div>
                <div>
                <img class="dx-comment-img" src="
                        <c:choose>
                        <c:when test="${!empty question.author.avatar}">
                            ${pageContext.request.contextPath}/controller?command=show_image&file=${question.author.avatar}
                        </c:when>
                        <c:otherwise>
                            ${pageContext.request.contextPath}/static/custom/images/noavatar.png
                        </c:otherwise>
                    </c:choose>
                    " alt="">
                    <span class="badge badge-${question.author.role.color} user-role-span" style="display: table;margin: 0 auto;margin-top:5px">
                        <fmt:message key="label.role.${fn:toLowerCase(question.author.role)}" var="roleName"/>
                        ${fn:substring(roleName, 0, 1)}
                    </span>
                </div>
                <div class="dx-comment-cont">
                    <a href="#" onclick="questions.showProfile('${pageContext.request.contextPath}', ${question.author.id}, event); return false;" class="dx-comment-name">${question.author.fio}</a>
                    <div class="dx-comment-date"><at:time-duration date="${question.creationDate}"/></div>
                    <div class="dx-comment-text">
                        <p class="mb-0">
                            ${question.text}
                        </p>
                    </div>
                    <ul class="attachments download" style="margin-top:10px;margin-bottom: 0px;">
                        <c:forEach var="a" items="${attachments}">
                            <li onclick="questions.downloadAttachment('${pageContext.request.contextPath}/controller?command=download&file=${a.file}')">
                                <span>
                                    <i class="fa fa-file file-attachment" aria-hidden="true"></i>${a.file}
                                </span>
                            </li>
                        </c:forEach>
                    </ul>
                    <c:if test="${!question.closed}">
                        <p></p>
                        <a class="reply-link" onclick="dataForms.reply('${question.author.fio}');"><i class="fa fa-reply" aria-hidden="true"></i> <fmt:message key="label.reply.button" /></a>
                    </c:if>
                </div>
            </div>
        </div>

        <c:forEach var="a" items="${answers}">
            <div class="dx-comment dx-ticket-comment">
                <div>
                    <div>
                        <img class="dx-comment-img" src="
                        <c:choose>
                        <c:when test="${!empty a.author.avatar}">
                            ${pageContext.request.contextPath}/controller?command=show_image&file=${a.author.avatar}
                        </c:when>
                        <c:otherwise>
                            ${pageContext.request.contextPath}/static/custom/images/noavatar.png
                        </c:otherwise>
                    </c:choose>
                    " alt="">
                        <span class="badge badge-${a.author.role.color} user-role-span" style="display: table;margin: 0 auto;margin-top:5px">
                        <fmt:message key="label.role.${fn:toLowerCase(a.author.role)}" var="roleName"/>
                        ${fn:substring(roleName, 0, 1)}
                    </span>
                    </div>
                    <div class="dx-comment-cont">
                        <a href="#" onclick="questions.showProfile('${pageContext.request.contextPath}', ${a.author.id}, event); return false;" class="dx-comment-name">${a.author.fio}</a>
                        <div class="dx-comment-date"><at:time-duration date="${a.creationDate}"/><c:if test="${!empty a.editingDate}"> (<fmt:message key="label.edit.short" />. <at:time-duration date="${a.editingDate}"/>)</c:if></div>
                        <div class="dx-comment-text">
                            <p class="mb-0">
                                    ${a.text}
                            </p>
                        </div>
                        <ul class="attachments download" style="margin-top:10px;margin-bottom: 10px;">
                            <c:forEach var="at" items="${a.attachments}">
                                <li onclick="questions.downloadAttachment('${pageContext.request.contextPath}/controller?command=download&file=${at.file}')">
                                <span>
                                    <i class="fa fa-file file-attachment" aria-hidden="true"></i>${at.file}
                                </span>
                                </li>
                            </c:forEach>
                        </ul>
                        <div style="font-size: 1.1em;">
                            <c:choose>
                                <c:when test="${a.rating > 0}">
                                    <c:set var="rating_color" value="success" />
                                </c:when>
                                <c:when test="${a.rating < 0}">
                                    <c:set var="rating_color" value="danger" />
                                </c:when>
                                <c:otherwise>
                                    <c:set var="rating_color" value="secondary" />
                                </c:otherwise>
                            </c:choose>

                            <c:choose>
                                <c:when test="${a.currentUserGrade < 0}">
                                    <a class="like-done" id="unlike_${a.id}"><i class="fas fa-thumbs-down"></i></a>
                                </c:when>
                                <c:otherwise>
                                    <a id="unlike_${a.id}" onclick="questions.likeAnswer('${pageContext.request.contextPath}', ${a.id}, false);" class="like-none"><i class="far fa-thumbs-down"></i></a>
                                </c:otherwise>
                            </c:choose>
                            <span class="badge badge-${rating_color}" style="cursor: default" id="rating_${a.id}">${a.rating}</span>
                            <c:choose>
                                <c:when test="${a.currentUserGrade > 0}">
                                    <a id="like_${a.id}" class="like-done"><i class="fas fa-thumbs-up"></i></a>
                                </c:when>
                                <c:otherwise>
                                    <a id="like_${a.id}" onclick="questions.likeAnswer('${pageContext.request.contextPath}', ${a.id}, true);" class="like-none"><i class="far fa-thumbs-up"></i></a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <c:if test="${!question.closed}">
                    <a class="reply-link" onclick="dataForms.reply('${a.author.fio}');"><i class="fa fa-reply" aria-hidden="true"></i> <fmt:message key="label.reply.button" /></a>
                    <a class="solution-link" onclick="" data-toggle="tooltip" data-placement="right" title="<fmt:message key="label.solution" />"><i class="far fa-check-square"></i></a>
                    </c:if>
                </div>
            </div>
        </c:forEach>
        <c:if test="${question.closed || totalPages > 1}">
            <jsp:include page="fragment/pagination.jsp" />
        </c:if>
        <c:if test="${!question.closed}">
            <div class="dx-comment dx-ticket-comment dx-comment-replied dx-comment-new" style="margin-bottom: -20px;">
            <div>
                <div class="dx-comment-img">
                    <img src="
                        <c:choose>
                        <c:when test="${!empty principal.avatar}">
                            ${pageContext.request.contextPath}/controller?command=show_image&file=${principal.avatar}
                        </c:when>
                        <c:otherwise>
                            ${pageContext.request.contextPath}/static/custom/images/noavatar.png
                        </c:otherwise>
                    </c:choose>
                    " alt="">
                </div>
                <div class="dx-comment-cont" style="width: 100%;">
                    <h3 class="h6 mb-10"><fmt:message key="label.reply" /></h3>
                    <textarea class="summernote" name="text" id="text" maxlength="${AppProperty.APP_ANSWER_MAXLENGTH}"></textarea>
                    <fmt:message key="label.textarea.remaining" /> <span id="text-counter">${AppProperty.APP_ANSWER_MAXLENGTH}</span>
                    <div class="form-group">
                        <label class="" for="file-selector" data-toggle="popover" data-trigger="hover" data-placement="bottom" style="cursor:pointer;"
                               data-content="<fmt:message key="message.attachment.info.part1" /> ${AppProperty.APP_ATTACHMENT_COUNT}. <fmt:message key="message.attachment.info.part2" /> ${AppProperty.APP_ATTACHMENT_SIZE} <fmt:message key="message.mb" />">
                            <input id="file-selector" type="file" multiple name="file" style="display:none">
                            <div style="margin: 10px 10px -10px 0px;">
                                <i class="fa fa-paperclip" aria-hidden="true"></i> <fmt:message key="label.uploadAttachments" />
                            </div>
                        </label>
                        <ul class="attachments" id="attachments-list" style="margin-top: 10px"></ul>
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">
                            <fmt:message key="label.reply.button" />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        </c:if>
        <c:if test="${question.closed && empty totalPages}">
            <div style="margin-top: -20px"></div>
        </c:if>
    </div>

<c:if test="${!question.closed}">
<script>
    $(function () {
        dataForms.initSummernote('text', '<fmt:message key="label.reply.placeholder" />', '${!empty current_lang ? current_lang : 'ru'}', 120);
        attacher.init('file-selector', 'attachments-list', ${AppProperty.APP_ATTACHMENT_COUNT}, ${AppProperty.APP_ATTACHMENT_SIZE});
    })
</script>
</c:if>
<jsp:include page="fragment/showProfileModal.jsp" />
<jsp:include page="layout/footer.jsp" />