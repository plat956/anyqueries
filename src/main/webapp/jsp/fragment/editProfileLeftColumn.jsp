<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="col-sm-3">
    <div class="text-center">
        <img src="${pageContext.request.contextPath}${!empty principal.avatar ? principal.avatar : '/static/custom/images/noavatar.png'}" class="avatar img-circle img-thumbnail" alt="avatar">
        <div class="panel panel-default text-center profile-role">
            <div class="panel-body">
                <span class="badge badge-${principal.role.color} user-role-span">${fn:toLowerCase(principal.role)}</span>
            </div>
        </div>
        <form id="avatar-form" action="${pageContext.request.contextPath}/controller?command=upload_avatar" method="post" enctype="multipart/form-data">
            <label class="btn btn-primary" for="file-selector">
                <input id="file-selector" type="file" accept=".png, .jpg, .jpeg" style="display:none" onchange="dataForms.uploadAvatar(this, 'avatar-form');">
                <fmt:message key="label.uploadAvatar" />
            </label>
        </form>
    </div>
    <ul class="list-group">
        <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.questions" /></strong></span> 125</li>
        <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.answers" /></strong></span> 13</li>
    </ul>
</div>