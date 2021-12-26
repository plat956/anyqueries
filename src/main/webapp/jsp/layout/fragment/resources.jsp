<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link type="image/x-icon" href="${pageContext.request.contextPath}/static/custom/images/favicon.ico" rel="icon" />
<script src="${pageContext.request.contextPath}/static/jquery/1.10.2/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/popperjs/1.12.5/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/static/bootstrap/4.2.1/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/static/bootstrap/4.2.1/js/bootstrap-select.min.js"></script>
<script src="${pageContext.request.contextPath}/static/bootstrap/4.2.1/js/bootstrap-typeahead.min.js"></script>
<script src="${pageContext.request.contextPath}/static/bootstrap/4.2.1/js/toastr.min.js"></script>
<script src="${pageContext.request.contextPath}/static/bootstrap/4.2.1/js/sweetalert.min.js"></script>
<script src="${pageContext.request.contextPath}/static/nprogress/0.2.0/js/nprogress.js"></script>
<c:choose>
    <c:when test="${!empty current_lang}">
        <script src="${pageContext.request.contextPath}/static/custom/js/i18n/${current_lang}.js"></script>
    </c:when>
    <c:otherwise>
        <script src="${pageContext.request.contextPath}/static/custom/js/i18n/ru.js"></script>
    </c:otherwise>
</c:choose>
<script src="${pageContext.request.contextPath}/static/custom/js/common.js"></script>
<link href="${pageContext.request.contextPath}/static/bootstrap/4.2.1/css/bootstrap.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/static/bootstrap/4.2.1/css/bootstrap-select.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/static/bootstrap/4.2.1/css/toastr.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/static/bootstrap/4.2.1/css/sweetalert.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/static/font-awesome/5.15.4/css/all.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/static/nprogress/0.2.0/css/nprogress.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/static/custom/css/common.css" rel="stylesheet" />