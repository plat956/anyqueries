<%@ page contentType="text/html;charset=UTF-8" language="java" import="by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary navbar-main-panel">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/controller?command=questions_page">
        <i class="fa fa-question-circle"></i>
        ${AppProperty.APP_NAME}
    </a>
    <c:if test="${!bad_browser_command}">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <form action="${pageContext.request.contextPath}/controller" id="searchForm" method="get" autocomplete="off">
                        <c:choose>
                            <c:when test="${param['command'] == 'categories_page'}">
                                <input type="hidden" name="command" value="categories_page" />
                                <fmt:message key="label.search.categories.placeholder" var="search_placeholder" />
                            </c:when>
                            <c:when test="${!empty principal && principal.role == 'ADMIN' && param['command'] == 'users_page'}">
                                <input type="hidden" name="command" value="users_page" />
                                <fmt:message key="label.search.users.placeholder" var="search_placeholder" />
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" name="command" value="questions_page" />
                                <c:choose>
                                    <c:when test="${!empty param['category']}">
                                        <fmt:message key="label.search.category.placeholder" var="search_placeholder" />
                                        <c:set var="search_placeholder" value="${search_placeholder} «${category_name}»" />
                                    </c:when>
                                    <c:when test="${!empty principal && param['mode'] == 'my'}">
                                        <fmt:message key="label.search.my.placeholder" var="search_placeholder" />
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="label.search.placeholder" var="search_placeholder" />
                                    </c:otherwise>
                                </c:choose>

                            </c:otherwise>
                        </c:choose>
                        <c:if test="${param['command'] == 'questions_page'}">
                            <c:if test="${!empty principal && !empty param['mode']}"><input type="hidden" name="mode" value="${param['mode']}" /></c:if>
                            <c:if test="${!empty param['sort']}"><input type="hidden" name="sort" value="${param['sort']}" /></c:if>
                            <c:if test="${!empty param['resolved']}"><input type="hidden" name="resolved" value="${param['resolved']}" /></c:if>
                            <c:if test="${!empty param['category']}"><input type="hidden" name="category" value="${param['category']}" /></c:if>
                        </c:if>
                        <div class="navbar-search input-group">
                            <input class="form-control py-2 border-left-0 border search-input" type="text" value="${fn:substring(param['query'], 0, 40)}"
                                   placeholder="${search_placeholder}" id="search-input" name="query" maxlength="40"/>
                            <div id="clearSearch" onclick="dataForms.clearSearch();" style="opacity: ${!empty param['query'] ? '1' : '0'}">
                                &times;
                            </div>
                            <span class="input-group-append" style="margin-left: -15px;">
                                <button type="submit" id="searchButton" class="no-loader navbar-search-btn btn btn-outline-primary border-left-0 border">
                                    <i class="fa fa-search"></i>
                                </button>
                            </span>
                        </div>
                    </form>
                </li>
            </ul>
            <ul class="navbar-nav form-inline my-2 my-lg-0">
                <c:choose>
                    <c:when test="${!empty principal}">
                        <span class="badge badge-${principal.role.color} user-role-span" id="navbar-role"><fmt:message key="label.role.${fn:toLowerCase(principal.role)}" /></span>
                        <li class="nav-item dropdown user-dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                               data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <c:choose>
                                    <c:when test="${!empty principal.avatar}">
                                        <img src="${pageContext.request.contextPath}/controller?command=show_image&file=${principal.avatar}" width="35" height="35" class="rounded-circle">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/static/custom/images/noavatar.png" width="35" height="35" class="rounded-circle">
                                    </c:otherwise>
                                </c:choose>
                                    ${principal.fio}
                            </a>
                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/controller?command=edit_profile_page"><fmt:message key="label.profile" /></a>
                                <div class="dropdown-divider"></div>
                                <form id="logout_form" action="${pageContext.request.contextPath}/controller?command=logout" method="post" autocomplete="off"></form>
                                <a class="dropdown-item" href="#" onclick="$('#logout_form').submit();"><fmt:message key="label.logout" /></a>
                            </div>
                        </li>
                    </c:when>
                    <c:when test="${!empty inactivePrincipal}">
                        <li class="nav-item">
                            <form id="logout_form" action="${pageContext.request.contextPath}/controller?command=logout" method="post" autocomplete="off"></form>
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
    </c:if>
</nav>
