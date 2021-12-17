<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/static/jquery/1.10.2/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap/4.0.0/js/bootstrap.js"></script>
    <link href="${pageContext.request.contextPath}/static/bootstrap/4.0.0/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/custom/css/common.css"/>
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary navbar-main-panel">
        <a class="navbar-brand" href="/controller?command=main_page">${fn:toUpperCase(initParam['website'])}</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <div class="navbar-search input-group">
                        <input class="form-control py-2 border-left-0 border" type="search"
                               placeholder="Поиск по вопросам" id="example-search-input"/>
                        <span class="input-group-append">
                            <button class="navbar-search-btn btn btn-outline-primary border-left-0 border">
                                <i class="fa fa-search"></i>
                            </button>
                        </span>
                    </div>
                </li>
            </ul>
            <ul class="navbar-nav form-inline my-2 my-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="/controller?command=login_page">Вход</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/controller?command=registration_page">Регистрация</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        username
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="/controller?command=profile_page">Профиль</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="/controller?command=logout">Выход</a>
                    </div>
                </li>
            </ul>
        </div>
    </nav>
    <section class="page-section content">
        <div class="row">