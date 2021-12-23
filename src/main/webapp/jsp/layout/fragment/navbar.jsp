<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary navbar-main-panel">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/controller?command=main_page">
        <i class="fa fa-question-circle"></i>
        ${fn:toUpperCase(initParam['website'])}
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
                           placeholder="Поиск по вопросам" id="example-search-input" />
                    <span class="input-group-append">
                        <button class="navbar-search-btn btn btn-outline-primary border-left-0 border">
                            <i class="fa fa-search"></i>
                        </button>
                    </span>
                </div>
            </li>
        </ul>
        <ul class="navbar-nav form-inline my-2 my-lg-0">
            <c:if test="${empty principal}">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=login_page">Вход</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=registration_page">Регистрация</a>
                </li>
            </c:if>
            <c:if test="${!empty principal}">
                <span class="badge badge-warning user-role-span">${fn:toLowerCase(principal.role)}</span>
                <li class="nav-item dropdown user-dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <img src="https://s3.eu-central-1.amazonaws.com/bootstrapbaymisc/blog/24_days_bootstrap/fox.jpg" width="35" height="35" class="rounded-circle">
                        ${principal.login}
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/controller?command=profile_page">Профиль</a>
                        <div class="dropdown-divider"></div>
                        <form id="logout_form" action="${pageContext.request.contextPath}/controller?command=logout" method="post" />
                        <a class="dropdown-item" href="#" onclick="$('#logout_form').submit();">Выход</a>
                    </div>
                </li>
            </c:if>
        </ul>
    </div>
</nav>
