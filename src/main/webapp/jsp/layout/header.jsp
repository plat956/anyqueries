<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.CookieName" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="current_lang" value="${cookie[CookieName.LANG].value}" scope="request" />
<fmt:setLocale value="${current_lang}" scope="request"/>
<fmt:setBundle basename="message" scope="request"/>
<c:if test="${empty page_title_label}">
    <c:set var="page_title_label" value="label.unknown-page" />
</c:if>
<fmt:message key="${page_title_label}" var="page_title" scope="request" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${fn:toUpperCase(initParam['website'])} | ${page_title}</title>
    <jsp:include page="fragment/resources.jsp" />
</head>
<body>
<div class="container">
    <jsp:include page="fragment/navbar.jsp" />
    <section class="page-section content">
        <div class="row sidebar-row">
        <jsp:include page="fragment/sidebar.jsp" />
        <div class="col-lg-9 custom-content">
            <jsp:include page="fragment/globalMessage.jsp" />
            <div class="grid support-content">
                <div class="grid-body">
                    <h5>${page_title}</h5>
                    <hr class="page-title-hr">
                    <jsp:include page="fragment/message.jsp" />