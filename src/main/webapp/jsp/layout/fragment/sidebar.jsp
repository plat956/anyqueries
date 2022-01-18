<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="wrongCategoryParam">
    <fmt:parseNumber var="categoryParam" type="number" integerOnly="true" value="${param['category']}" />
</c:catch>
<div class="custom-sidebar col-lg-3">
    <div class="grid support">
        <div class="grid-body">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                <c:if test="${empty categoryParam}">
                    <c:if test="${param['command'] == 'questions_page' && (param['mode'] != 'my' || empty principal)}">
                        <c:set var="questionsPage" value="true" />
                    </c:if>
                    <c:if test="${param['command'] == 'questions_page' && param['mode'] == 'my' && !empty principal}">
                        <c:set var="myQuestionsPage" value="true" />
                    </c:if>
                </c:if>
                <a class="nav-link${questionsPage ? ' active bg-primary' : ''}" href="<c:if test="${!questionsPage}">${pageContext.request.contextPath}/controller?command=questions_page</c:if>" aria-selected="true">
                    <fmt:message key="label.allQuestions" /> <span class="badge badge-${questionsPage ? 'light' : 'primary'}">${layoutTotalQuestions}</span>
                </a>
                <c:if test="${!empty principal}">
                    <a class="nav-link${myQuestionsPage ? ' active bg-primary' : ''}" href="<c:if test="${!myQuestionsPage}">${pageContext.request.contextPath}/controller?command=questions_page&mode=my</c:if>" aria-selected="false">
                        <fmt:message key="label.myQuestions" /> <span class="badge badge-${myQuestionsPage ? 'light' : 'primary'}">${layoutTotalUserQuestions}</span>
                    </a>
                </c:if>
                <c:if test="${!empty principal && principal.role == 'ADMIN'}">
                    <a class="nav-link${param['command'] == 'users_page' ? ' active bg-primary' : ''}" href="${pageContext.request.contextPath}/controller?command=users_page" aria-selected="false"><fmt:message key="label.users" /></a>
                </c:if>
            </div>
            <hr>
            <h6 class="sidebar-label"><fmt:message key="label.categories" /></h6>
            <ul class="support-label">
                <c:forEach var="c" items="${layoutTopCategories}">
                    <li>
                        <a href="<c:if test="${!empty categoryParam && categoryParam != c.id}">${pageContext.request.contextPath}/controller?command=questions_page&category=${c.id}</c:if>">
                            <span class="support-label-span" style="background-color: ${c.color}">&#xA0;</span>${c.name}<span class="float-right">${c.questionsCount}</span>
                        </a>
                    </li>
                </c:forEach>
            </ul>
            <a class="btn btn-light btn-sm other-categories" href="${pageContext.request.contextPath}/controller?command=categories_page">
                <fmt:message key="label.allCategories" />
            </a>
            <hr>
            <form method="post" action="${pageContext.request.contextPath}/controller?command=change_locale" id="locale_form" autocomplete="off">
                <select class="selectpicker" data-width="100%" name="lang" onchange="$('#locale_form').submit();">
                    <option  data-content='<i class="flag flag-russia"></i> <fmt:message key="label.lang.ru" />' value="ru"${current_lang == 'ru' ? ' selected' : ''}><fmt:message key="label.lang.ru" /></option>
                    <option  data-content='<i class="flag flag-belarus"></i> <fmt:message key="label.lang.be" />' value="be"${current_lang == 'be' ? ' selected' : ''}><fmt:message key="label.lang.be" /></option>
                    <option data-content='<i class="flag flag-united-states"></i> <fmt:message key="label.lang.en" />' value="en"${current_lang == 'en' ? ' selected' : ''}><fmt:message key="label.lang.en" /></option>
                </select>
            </form>
        </div>
    </div>
</div>