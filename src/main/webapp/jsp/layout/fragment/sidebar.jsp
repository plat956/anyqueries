<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="custom-sidebar col-lg-3">
    <div class="grid support">
        <div class="grid-body">
            <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                <a class="nav-link active bg-primary" href="${pageContext.request.contextPath}/" aria-selected="true"><fmt:message key="label.allQuestions" /> <span class="badge badge-light">44 todo</span></a>
                <c:if test="${!empty principal}">
                    <a class="nav-link " href="${pageContext.request.contextPath}/" aria-selected="false"><fmt:message key="label.myQuestions" /> <span class="badge badge-primary">1 todo</span></a>
                </c:if>
                <a class="nav-link" href="${pageContext.request.contextPath}/" aria-selected="false"><fmt:message key="label.leadersList" /></a>
            </div>
            <hr>
            <strong><fmt:message key="label.categories" /></strong>
            <ul class="support-label">
                <li><a href="${pageContext.request.contextPath}/"><span class="support-label-span bg-blue">&#xA0;</span>Природа todo<span class="float-right">2 todo</span></a>
                </li>
                <li><a href="${pageContext.request.contextPath}/"><span class="support-label-span bg-red">&#xA0;</span>Наука todo<span class="float-right">7 todo</span></a>
                </li>
                <li><a href="${pageContext.request.contextPath}/"><span class="support-label-span bg-yellow">&#xA0;</span>Кулинария todo<span class="float-right">128 todo</span></a>
                </li>
            </ul>
            <hr>
            <form method="post" action="${pageContext.request.contextPath}/controller?command=change_locale" id="locale_form">
            <select class="selectpicker" data-width="100%" name="lang" onchange="$('#locale_form').submit();">
                <option  data-content='<i class="flag flag-russia"></i> <fmt:message key="label.lang.ru" />' value="ru"${empty current_lang || current_lang == 'ru' ? ' selected' : ''}><fmt:message key="label.lang.ru" /></option>
                <option  data-content='<i class="flag flag-belarus"></i> <fmt:message key="label.lang.be" />' value="be"${current_lang == 'be' ? ' selected' : ''}><fmt:message key="label.lang.be" /></option>
                <option data-content='<i class="flag flag-united-states"></i> <fmt:message key="label.lang.en" />' value="en"${current_lang == 'en' ? ' selected' : ''}><fmt:message key="label.lang.en" /></option>
            </select>
            </form>
        </div>
    </div>
</div>