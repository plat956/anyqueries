<%@ page contentType="text/html;charset=UTF-8" language="java" import="by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary navbar-main-panel">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/controller?command=main_page">
        <i class="fa fa-question-circle"></i>
        ${AppProperty.APP_NAME}
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <div class="navbar-search input-group">
                    <input class="form-control py-2 border-left-0 border search-input" type="text"
                           placeholder="<fmt:message key="label.search.placeholder" />" id="example-search-input" />
                    <span class="input-group-append">
                        <button class="navbar-search-btn btn btn-outline-primary border-left-0 border">
                            <i class="fa fa-search"></i>
                        </button>
                    </span>
                </div>
            </li>
        </ul>
        <ul class="navbar-nav form-inline my-2 my-lg-0">
            <c:choose>
                <c:when test="${!empty principal}">
                    <span class="badge badge-${principal.role.color} user-role-span"><fmt:message key="label.role.${fn:toLowerCase(principal.role)}" /></span>
                    <li class="nav-item dropdown user-dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <img src="${pageContext.request.contextPath}${!empty principal.avatar ? principal.avatar : '/static/custom/images/noavatar.png'}" width="35" height="35" class="rounded-circle">
                                ${principal.fio}
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/controller?command=edit_profile_page"><fmt:message key="label.profile" /></a>
                            <div class="dropdown-divider"></div>
                            <form id="logout_form" action="${pageContext.request.contextPath}/controller?command=logout" method="post"></form>
                            <a class="dropdown-item" href="#" onclick="$('#logout_form').submit();"><fmt:message key="label.logout" /></a>
                        </div>
                    </li>
                </c:when>
                <c:when test="${!empty inactivePrincipal}">
                    <li class="nav-item">
                        <form id="logout_form" action="${pageContext.request.contextPath}/controller?command=logout" method="post"></form>
                        <a class="nav-link" href="#" onclick="$('#logout_form').submit();"><fmt:message key="label.logout" /></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=login_page"><fmt:message key="label.login" /></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=registration_page"><fmt:message key="label.registration" /></a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
