<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        <div class="row">
        <jsp:include page="fragment/sidebar.jsp" />
        <div class="col-lg-9">
            <jsp:include page="fragment/globalErrors.jsp" />
            <div class="grid support-content">
                <div class="grid-body">
                    <h5>${page_title}</h5>
                    <hr class="page-title-hr">
                    <jsp:include page="fragment/message.jsp" />