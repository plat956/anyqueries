<%@ page contentType="text/html;charset=UTF-8" language="java" import="by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ft" uri="formtags" %>
<div class="col-sm-3">
    <div class="text-center">
        <c:choose>
            <c:when test="${!empty principal.avatar}">
                <img src="${pageContext.request.contextPath}/controller?command=show_image&file=${principal.avatar}" class="avatar img-circle img-thumbnail" alt="avatar">
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/static/custom/images/noavatar.png'}" class="avatar img-circle img-thumbnail" alt="avatar">
            </c:otherwise>
        </c:choose>
        <div class="panel panel-default text-center profile-role">
            <div class="panel-body">
                <span class="badge badge-${principal.role.color} user-role-span"><fmt:message key="label.role.${fn:toLowerCase(principal.role)}" /></span>
            </div>
        </div>
        <form id="avatar-form" action="${pageContext.request.contextPath}/controller?command=upload_avatar" method="post" enctype="multipart/form-data">
            <label class="btn btn-primary" for="file-selector">
                <input id="file-selector" type="file" name="file" accept="<ft:list-formatter list="${AppProperty.APP_UPLOAD_AVATAR_EXTENSIONS}" />" style="display:none" onchange="dataForms.uploadAvatar(this, 'avatar-form');">
                <fmt:message key="label.uploadAvatar" />
            </label>
        </form>
    </div>
    <ul class="list-group">
        <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.questions" /></strong></span> 125</li>
        <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.answers" /></strong></span> 13</li>
    </ul>
</div>