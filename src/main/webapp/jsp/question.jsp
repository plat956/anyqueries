<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
         by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:catch var="wrongEditParam">
    <fmt:parseNumber var="editParam" type="number" integerOnly="true" value="${param['edit']}" />
</c:catch>
<c:choose>
    <c:when test="${empty editParam}">
        <c:set var="page_title" value="${question.title}" scope="request" />
    </c:when>
    <c:otherwise>
        <c:set var="page_title_label" value="label.answer.editing" scope="request" />
        <c:set var="page_title_prefix" value="${question.title} | " scope="request" />
    </c:otherwise>
</c:choose>
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
                    <span class="badge badge-${question.closed ? 'secondary' : 'success'} user-role-span" style="font-size: 0.9em"><fmt:message key="label.status.${question.closed ? 'closed' : 'open'}" /></span>
                </label>
                <c:if test="${!empty principal && principal.id == question.author.id}">
                    <label class="switch" style="margin-top: -5px;">
                        <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="status" id="status" onchange="questions.changeStatus('${pageContext.request.contextPath}', this, ${question.id});"${question.closed ? ' checked' : ''}>
                        <span class="slider slider-light round"></span>
                    </label>
                </c:if>
                </span></li>
                <li><span><span class="dx-blog-post-info-title"><fmt:message key="label.modified" /></span>
                    <at:time-duration date="${!empty question.editingDate ? question.editingDate : question.creationDate}"/>
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

                    <span id="dropdownMenuButton${a.id}" data-toggle="dropdown">
                        <c:if test="${!empty principal.id}">
                            <c:set var="edit_access" value="${!question.closed && principal.id == question.author.id}" />
                            <c:set var="delete_access" value="${principal.role == 'ADMIN' || principal.role == 'MODERATOR' || principal.id == question.author.id}" />
                            <c:if test="${edit_access || delete_access}">
                                <a  class="edit-answer-btn" data-toggle="tooltip" data-placement="right" title="<fmt:message key="label.question.management" />">
                                    <i class="fas fa-cog"></i>
                                </a>
                                <div class="dropdown-menu answer-dropdown" aria-labelledby="dropdownMenuButton${question.id}">
                                    <c:if test="${edit_access}">
                                        <a class="dropdown-item drop-lnk" onclick="location.href = '${pageContext.request.contextPath}/controller?command=edit_question_page&id=${question.id}'"><i class="fa fa-edit" aria-hidden="true" style="color: #007bff"></i> <fmt:message key="label.edit" /></a>
                                    </c:if>
                                    <c:if test="${delete_access}">
                                        <a class="dropdown-item drop-lnk" onclick="questions.delete(event, '${pageContext.request.contextPath}', ${question.id}, true)"><i class="fa fa-trash" aria-hidden="true" style="color: red"></i> <fmt:message key="label.delete" /></a>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:if>
                    </span>
                    <div class="dx-comment-date"><at:time-duration date="${question.creationDate}"/><c:if test="${!empty question.editingDate}"> (<fmt:message key="label.edit.short" />. <at:time-duration date="${question.editingDate}"/>)</c:if></div>
                    <div class="dx-comment-text">
                        <p class="mb-0">
                            ${question.text}
                        </p>
                    </div>
                    <ul class="attachments download" style="margin-top:10px;margin-bottom: 0px;">
                        <c:forEach var="a" items="${attachments}">
                            <li onclick="questions.downloadAttachment('${pageContext.request.contextPath}/controller?command=download&file=${a.file}')">
                                <span>
                                    <i class="fa fa-file file-attachment file-i" aria-hidden="true"></i>${a.file}
                                </span>
                            </li>
                        </c:forEach>
                    </ul>
                    <c:if test="${!question.closed && !empty principal && question.author.id != principal.id && empty editParam}">
                        <p></p>
                        <a class="reply-link" onclick="dataForms.reply('${question.author.fio}');"><i class="fa fa-reply" aria-hidden="true"></i> <fmt:message key="label.reply.button" /></a>
                    </c:if>
                </div>
            </div>
        </div>

        <c:forEach var="a" items="${answers}">
            <div class="dx-comment dx-ticket-comment" id="answer-box-${a.id}">
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
                            <span id="dropdownMenuButton${a.id}" data-toggle="dropdown">
                                <c:if test="${!empty principal.id}">
                                    <c:set var="edit_access" value="${!question.closed && principal.id == a.author.id}" />
                                    <c:set var="delete_access" value="${principal.role == 'ADMIN' || principal.role == 'MODERATOR' || principal.id == a.author.id}" />
                                    <c:if test="${edit_access || delete_access}">
                                        <a class="edit-answer-btn" data-toggle="tooltip" data-placement="right" title="<fmt:message key="label.answer.management" />">
                                            <i class="fas fa-cog"></i>
                                        </a>
                                        <div class="dropdown-menu answer-dropdown" aria-labelledby="dropdownMenuButton${a.id}">
                                            <c:if test="${edit_access && editParam != a.id}">
                                                <a class="dropdown-item drop-lnk" onclick="location.href = '<at:query-parameter-changer key="edit" value="${a.id}"/>'"><i class="fa fa-edit" aria-hidden="true" style="color: #007bff"></i> <fmt:message key="label.edit" /></a>
                                            </c:if>
                                            <c:if test="${delete_access}">
                                                <a class="dropdown-item drop-lnk" onclick="answers.delete(event, '${pageContext.request.contextPath}', ${a.id})"><i class="fa fa-trash" aria-hidden="true" style="color: red"></i> <fmt:message key="label.delete" /></a>
                                            </c:if>
                                        </div>
                                    </c:if>
                                </c:if>
                            </span>
                            <div class="dx-comment-date"><at:time-duration date="${a.creationDate}"/><c:if test="${!empty a.editingDate}"> (<fmt:message key="label.edit.short" />. <at:time-duration date="${a.editingDate}"/>)</c:if></div>
                            <c:if test="${editParam != a.id || question.closed}">
                                <div id="comment-data-${a.id}">
                                    <div class="dx-comment-text">
                                        <p class="mb-0">
                                                ${a.text}
                                        </p>
                                    </div>
                                    <ul class="attachments download" style="margin-top:10px;margin-bottom: 10px;">
                                        <c:forEach var="at" items="${a.attachments}">
                                            <li onclick="questions.downloadAttachment('${pageContext.request.contextPath}/controller?command=download&file=${at.file}')">
                                            <span>
                                                <i class="fa fa-file file-attachment file-i" aria-hidden="true"></i>${at.file}
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
                                                <c:if test="${!empty principal && principal.id != a.author.id}">
                                                    <a id="unlike_${a.id}" onclick="answers.like('${pageContext.request.contextPath}', ${a.id}, false);" class="like-none"><i class="far fa-thumbs-down"></i></a>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="badge badge-${rating_color}" style="cursor: default" id="rating_${a.id}">${a.rating}</span>
                                        <c:choose>
                                            <c:when test="${a.currentUserGrade > 0}">
                                                <a id="like_${a.id}" class="like-done"><i class="fas fa-thumbs-up"></i></a>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${!empty principal && principal.id != a.author.id}">
                                                    <a id="like_${a.id}" onclick="answers.like('${pageContext.request.contextPath}', ${a.id}, true);" class="like-none"><i class="far fa-thumbs-up"></i></a>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${editParam == a.id && !question.closed}">
                                <div id="comment-edit-data-${a.id}">
                                    <form class="needs-validation" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/controller?command=edit_answer" novalidate autocomplete="off">
                                        <input type="hidden" value="${a.id}" name="id" />
                                        <textarea class="summernote form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.TEXT)}" />" name="text" id="answer_text${a.id}" maxlength="${AppProperty.APP_ANSWER_MAXLENGTH}" required>${!empty validationResult ? validationResult.getValue(RequestParameter.TEXT) : a.text}</textarea>
                                        <c:if test="${!empty validationResult.getMessage(RequestParameter.TEXT)}">
                                            <div class="invalid-feedback-backend">
                                                <fmt:message key="${validationResult.getMessage(RequestParameter.TEXT)}" />
                                            </div>
                                        </c:if>
                                        <div class="invalid-feedback">
                                            <fmt:message key="label.wrong-input" />
                                        </div>
                                        <fmt:message key="label.textarea.remaining" /> <span id="answer_text${a.id}-counter">${AppProperty.APP_ANSWER_MAXLENGTH}</span>
                                        <div class="form-group">
                                            <label class="" for="file-selector${a.id}" data-toggle="popover" data-trigger="hover" data-placement="bottom" style="cursor:pointer;height: 40px;margin-bottom: 0px;"
                                                   data-content="<fmt:message key="message.attachment.info.part1" /> ${AppProperty.APP_ATTACHMENT_COUNT}. <fmt:message key="message.attachment.info.part2" /> ${AppProperty.APP_ATTACHMENT_SIZE} <fmt:message key="message.mb" />">
                                                <input id="file-selector${a.id}" type="file" multiple name="file" style="display:none">
                                                <div style="margin: 10px 10px -10px 0px;">
                                                    <i class="fa fa-paperclip" aria-hidden="true"></i> <fmt:message key="label.uploadAttachments" />
                                                </div>
                                            </label>
                                            <ul class="attachments" id="attachments-list${a.id}" style="margin-top: 10px">
                                                <c:forEach var="c" items="${a.attachments}">
                                                    <li>
                                                        <span>
                                                            <i class="fa fa-file file-attachment file-i" aria-hidden="true"></i>${c.file}
                                                        </span>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>

                                        <div class="form-group">
                                            <button type="submit" class="btn btn-primary" id="replyButton${a.id}">
                                                <fmt:message key="label.save.button" />
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </c:if>
                        </div>
                        <c:if test="${!question.closed}">
                            <c:if test="${!empty principal && a.author.id != principal.id && empty editParam}">
                                <a class="reply-link" id="reply-link${a.id}" onclick="dataForms.reply('${a.author.fio}');"><i class="fa fa-reply" aria-hidden="true"></i> <fmt:message key="label.reply.button" /></a>
                            </c:if>
                        </c:if>
                        <c:if test="${a.solution || (!empty principal && principal.id == question.author.id && !question.closed)}">
                            <a class="solution-link<c:if test="${a.solution}"> solution-mark</c:if>" id="solution_${a.id}"
                               <c:if test="${!question.closed && !empty principal && principal.id == question.author.id}">
                                   onclick="answers.markAsSolution('${pageContext.request.contextPath}', '${a.id}');"
                                   data-toggle="tooltip" data-placement="right" title="<fmt:message key="label.solution${a.solution ? '.remove' : ''}" />"
                                   data-def-title="<fmt:message key="label.solution" />" data-rm-title="<fmt:message key="label.solution.remove" />"
                                   style="cursor: pointer"
                               </c:if>
                            >
                                <i class="fa${a.solution ? 's' : 'r'} fa-check-square"></i>
                            </a>
                        </c:if>
                </div>
            </div>
        </c:forEach>
        <c:if test="${question.closed || totalPages > 1}">
            <jsp:include page="fragment/pagination.jsp" />
        </c:if>
        <c:if test="${!empty principal && !question.closed && empty editParam}">
            <div class="dx-comment dx-ticket-comment dx-comment-replied dx-comment-new" style="margin-bottom: -20px;" id="replyForm">
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
                    <form id="answerForm" class="needs-validation" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/controller?command=create_answer" novalidate autocomplete="off">
                        <input type="hidden" value="${question.id}" name="question" />
                        <h3 class="h6 mb-10"><fmt:message key="label.reply" /></h3>
                        <textarea class="summernote form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.TEXT)}" />" name="text" id="text" maxlength="${AppProperty.APP_ANSWER_MAXLENGTH}" required>${validationResult.getValue(RequestParameter.TEXT)}</textarea>
                        <c:if test="${!empty validationResult.getMessage(RequestParameter.TEXT)}">
                            <div class="invalid-feedback-backend">
                                <fmt:message key="${validationResult.getMessage(RequestParameter.TEXT)}" />
                            </div>
                        </c:if>
                        <div class="invalid-feedback">
                            <fmt:message key="label.wrong-input" />
                        </div>
                        <fmt:message key="label.textarea.remaining" /> <span id="text-counter">${AppProperty.APP_ANSWER_MAXLENGTH}</span>
                        <div class="form-group">
                            <label class="" for="file-selector" data-toggle="popover" data-trigger="hover" data-placement="bottom" style="cursor:pointer;height: 40px;margin-bottom: 0px;"
                                   data-content="<fmt:message key="message.attachment.info.part1" /> ${AppProperty.APP_ATTACHMENT_COUNT}. <fmt:message key="message.attachment.info.part2" /> ${AppProperty.APP_ATTACHMENT_SIZE} <fmt:message key="message.mb" />">
                                <input id="file-selector" type="file" multiple name="file" style="display:none">
                                <div style="margin: 10px 10px -10px 0px;">
                                    <i class="fa fa-paperclip" aria-hidden="true"></i> <fmt:message key="label.uploadAttachments" />
                                </div>
                            </label>
                            <ul class="attachments" id="attachments-list" style="margin-top: 10px"></ul>
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary" id="replyButton">
                                <fmt:message key="label.reply.button" />
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        </c:if>
        <c:if test="${(empty principal || !empty editParam) && totalPages <= 1}">
            <div style="margin-top: -20px"></div>
        </c:if>
    </div>
<c:if test="${!empty principal && !question.closed}">
<script>
    $(window).load(function() {
        <c:choose>
            <c:when test="${!empty editParam}">
                setTimeout(function () {
                    dataForms.scrollToDiv('answer-box-${editParam}');
                }, 1);
            </c:when>
            <c:otherwise>
                <c:if test="${!empty answerObject || !empty validationResult}">
                    <c:if test="${createRecord}">
                        pageEvents.scrollBottom(1);
                    </c:if>
                    <c:if test="${empty createRecord}">
                        setTimeout(function () {
                            dataForms.scrollToDiv('answer-box-${answerObject.id}');
                        }, 1);
                    </c:if>
                    <c:if test="${!empty answerObject}">
                        $('#answer-box-${answerObject.id}').css('background-color', 'rgb(255 230 170)');
                        $('#answer-box-${answerObject.id}').animate({backgroundColor: "#fff"}, 1000 );
                    </c:if>
                </c:if>
            </c:otherwise>
        </c:choose>
    });

    $(function () {
        <c:choose>
            <c:when test="${empty editParam}">
                dataForms.initSummernote('text', '<fmt:message key="label.reply.placeholder" />', '${current_lang}', 120, ${!empty validationResult.getMessage(RequestParameter.TEXT)});
                attacher.init('file-selector', 'attachments-list', ${AppProperty.APP_ATTACHMENT_COUNT}, ${AppProperty.APP_ATTACHMENT_SIZE});
            </c:when>
            <c:otherwise>
                dataForms.initSummernote('answer_text${editParam}', '<fmt:message key="label.reply.placeholder" />', '${current_lang}', 120, ${!empty validationResult.getMessage(RequestParameter.TEXT)});
                attacher.init('file-selector${editParam}', 'attachments-list${editParam}', ${AppProperty.APP_ATTACHMENT_COUNT}, ${AppProperty.APP_ATTACHMENT_SIZE});
            </c:otherwise>
        </c:choose>
    });
</script>
</c:if>
<jsp:include page="fragment/showProfileModal.jsp" />
<jsp:include page="layout/footer.jsp" />