<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="message.error500.description" scope="request" />
<jsp:include page="../layout/header.jsp" />
<div class="page-wrap d-flex flex-row align-items-center">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-12 text-center">
                <span class="display-1 d-block"><fmt:message key="label.error500.title" /></span>
                <div class="mb-4 lead"><fmt:message key="message.error500.description" /></div>
                <a href="#" onclick="window.history.back();" class="btn btn-success">
                    <fmt:message key="label.back" />
                </a>
                <a href="${pageContext.request.contextPath}/controller?command=questions_page" class="btn btn-secondary">
                    <fmt:message key="label.backHome" />
                </a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../layout/footer.jsp" />