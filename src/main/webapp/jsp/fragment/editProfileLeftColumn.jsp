<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="col-sm-3">
    <div class="text-center">
        <img src="${pageContext.request.contextPath}${!empty principal.avatar ? principal.avatar : '/static/custom/images/noavatar.png'}" class="avatar img-circle img-thumbnail" alt="avatar">
        <div class="panel panel-default text-center" style="margin-top:10px">
            <div class="panel-body">
                <span class="badge badge-${principal.role.color} user-role-span">${fn:toLowerCase(principal.role)}</span>
            </div>
        </div>
        <label class="btn btn-primary" for="my-file-selector" style="margin-top:10px">
            <input id="my-file-selector" type="file" style="display:none" onchange="">
            <fmt:message key="label.uploadAvatar" />
        </label>
    </div><br>
    <ul class="list-group">
        <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.questions" /></strong></span> 125</li>
        <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.answers" /></strong></span> 13</li>
    </ul>
</div>