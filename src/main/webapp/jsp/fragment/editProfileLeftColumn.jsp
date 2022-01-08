<%@ page contentType="text/html;charset=UTF-8" language="java" import="by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="at" uri="apptags" %>
<div class="col-sm-3">
    <div class="text-center">
        <c:choose>
            <c:when test="${!empty principal.avatar}">
                <img src="${pageContext.request.contextPath}/controller?command=show_image&file=${principal.avatar}" class="avatar img-circle img-thumbnail" alt="avatar">
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/static/custom/images/noavatar.png" class="avatar img-circle img-thumbnail" alt="avatar">
            </c:otherwise>
        </c:choose>
        <div class="panel panel-default text-center profile-role">
            <div class="panel-body">
                <span class="badge badge-${principal.role.color} user-role-span"><fmt:message key="label.role.${fn:toLowerCase(principal.role)}" /></span>
            </div>
        </div>
        <form id="avatar-form" action="${pageContext.request.contextPath}/controller?command=upload_avatar" method="post" enctype="multipart/form-data" autocomplete="off">
            <label class="btn btn-primary" for="file-selector" data-toggle="popover" data-trigger="hover" data-placement="bottom"
                   data-content="<fmt:message key="label.attachment.max.notice" /> ${AppProperty.APP_ATTACHMENT_SIZE} <fmt:message key="message.mb" />. <fmt:message key="label.attachment.formats" /> <at:list-formatter list="${AppProperty.APP_UPLOAD_AVATAR_EXTENSIONS}" />">
                <input id="file-selector" type="file" name="file" data-max-size="${AppProperty.APP_ATTACHMENT_SIZE}" accept="<at:list-formatter list="${AppProperty.APP_UPLOAD_AVATAR_EXTENSIONS}" />" style="display:none" onchange="dataForms.uploadAvatar(this, 'avatar-form');">
                <fmt:message key="label.uploadAvatar" />
            </label>
        </form>
    </div>
    <ul class="list-group">
        <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.questions" /></strong></span> ${totalQuestions}</li>
        <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.answers" /></strong></span> ${totalAnswers}</li>
    </ul>
</div>