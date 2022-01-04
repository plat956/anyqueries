<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="custom-sidebar col-lg-3">
    <div class="grid support">
        <div class="grid-body">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                <a class="nav-link active bg-primary" href="${pageContext.request.contextPath}/controller?command=questions_page" aria-selected="true">
                    <fmt:message key="label.allQuestions" /> <span class="badge badge-light">${layoutTotalQuestions}</span>
                </a>
                <c:if test="${!empty principal}">
                    <a class="nav-link " href="${pageContext.request.contextPath}/controller?command=questions_page&mode=my" aria-selected="false">
                        <fmt:message key="label.myQuestions" /> <span class="badge badge-primary">${layoutTotalUserQuestions}</span>
                    </a>
                </c:if>
                <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=leaderboard_page" aria-selected="false"><fmt:message key="label.leadersList" /></a>
            </div>
            <hr>
            <h6 class="sidebar-label"><fmt:message key="label.categories" /></h6>
            <ul class="support-label">
                <c:forEach var="c" items="${layoutTopCategories}">
                    <li>
                        <a href="${pageContext.request.contextPath}/controller?command=category_page&id=${c.id}">
                            <span class="support-label-span" style="background-color: ${c.color}">&#xA0;</span>${c.name}<span class="float-right">${c.questionsCount}</span>
                        </a>
                    </li>
                </c:forEach>
            </ul>
            <a class="btn btn-light btn-sm other-categories" href="${pageContext.request.contextPath}/controller?command=categories_page">
                <fmt:message key="label.allCategories" />
            </a>
            <c:if test="${principal.role == 'MODERATOR'}">
                <hr>
                <h6 class="sidebar-label"><fmt:message key="label.controlPanel" /></h6>
                <div class="nav flex-column nav-pills" role="tablist" aria-orientation="vertical">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=manage_categories_page" aria-selected="true"><fmt:message key="label.categories" /></a>
                    <a class="nav-link " href="${pageContext.request.contextPath}/controller?command=manage_users_page" aria-selected="false"><fmt:message key="label.users" /></a>
                </div>
            </c:if>
            <hr>
            <form method="post" action="${pageContext.request.contextPath}/controller?command=change_locale" id="locale_form" autocomplete="off">
                <select class="selectpicker" data-width="100%" name="lang" onchange="$('#locale_form').submit();">
                    <option  data-content='<i class="flag flag-russia"></i> <fmt:message key="label.lang.ru" />' value="ru"${empty current_lang || current_lang == 'ru' ? ' selected' : ''}><fmt:message key="label.lang.ru" /></option>
                    <option  data-content='<i class="flag flag-belarus"></i> <fmt:message key="label.lang.be" />' value="be"${current_lang == 'be' ? ' selected' : ''}><fmt:message key="label.lang.be" /></option>
                    <option data-content='<i class="flag flag-united-states"></i> <fmt:message key="label.lang.en" />' value="en"${current_lang == 'en' ? ' selected' : ''}><fmt:message key="label.lang.en" /></option>
                </select>
            </form>
        </div>
    </div>
</div>