<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${cookie['lang'].value}" scope="session" />
<fmt:setBundle basename="message" var="rb" scope="session" />
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
            <jsp:include page="fragment/badBrowser.jsp" />
            <div class="grid support-content">
                <div class="grid-body">
                    <h5>${page_title}</h5>
                    <hr class="page-title-hr">
                    <jsp:include page="fragment/message.jsp" />