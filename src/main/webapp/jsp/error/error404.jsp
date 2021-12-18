<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="page_title" value="Page not found" scope="request" />
<jsp:include page="../layout/header.jsp" />
<div class="page-wrap d-flex flex-row align-items-center">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-12 text-center">
                <span class="display-1 d-block">404</span>
                <div class="mb-4 lead">The page you are looking for was not found.</div>
                <a href="/" class="btn btn-success">Back to Home</a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../layout/footer.jsp" />