<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.CookieName,
         by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="current_lang" value="${cookie[CookieName.LANG].value}" scope="request" />
<fmt:setLocale value="${current_lang}" scope="request"/>
<fmt:setBundle basename="message" scope="request"/>
<c:if test="${empty page_title}">
    <c:set var="page_title" scope="request">
        <fmt:message key="${page_title_label}" />
    </c:set>
</c:if>
<c:if test="${empty page_title || fn:startsWith(page_title, '???')}">
    <fmt:message key="label.unknown-page" var="page_title" scope="request" />
</c:if>
<c:set var="bad_browser_command" value="${param['command'] == 'bad_browser_page'}" scope="request" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${AppProperty.APP_NAME} | ${page_title}</title>
    <jsp:include page="fragment/resources.jsp" />
    <c:if test="${!bad_browser_command}">
        <noscript>
            <meta http-equiv="refresh" content="0;url=${pageContext.request.contextPath}/controller?command=bad_browser_page">
        </noscript>
    </c:if>
</head>
<body onload="pageEvents.noBack();" onpageshow="if (event.persisted) pageEvents.noBack();" onunload="">
<div class="container" id="container" style="${!bad_browser_command ? 'display:none' : ''}">
    <jsp:include page="fragment/navbar.jsp" />
    <section class="page-section content">
        <div class="row sidebar-row">
        <c:if test="${!bad_browser_command}">
            <jsp:include page="fragment/sidebar.jsp" />
        </c:if>
        <div class="col-lg-${!bad_browser_command ? '9' : '12'} custom-content">
            <jsp:include page="fragment/globalMessage.jsp" />
            <div class="grid support-content">
                <div class="grid-body">
                    <h5>${page_title}${page_title_postfix}</h5>
                    <hr class="page-title-hr">
                    <jsp:include page="fragment/message.jsp" />