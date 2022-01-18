<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
         by.latushko.anyqueries.validator.ValidationPattern" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="label.editCategory" scope="request" />
<jsp:include page="layout/header.jsp" />
<form id="edit_form" class="needs-validation" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/controller?command=edit_category" novalidate autocomplete="off">
    <input type="hidden" name="id" value="${category.id}">
    <input type="hidden" name="previousPage" value="${empty previousPage ? header.referer : previousPage}">
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-folder-open" aria-hidden="true"></i> </span>
        </div>
        <input name="name" class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.NAME)}" />"
               placeholder="<fmt:message key="label.category.name" />" type="text" required pattern="${ValidationPattern.CATEGORY_NAME_REGEXP}" maxlength="25"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="<fmt:message key="label.category.name.info" />"
               value="${!empty validationResult.getValue(RequestParameter.NAME) ? validationResult.getValue(RequestParameter.NAME) : category.name}"
        >
        <c:if test="${!empty validationResult.getMessage(RequestParameter.NAME)}">
            <div class="invalid-feedback-backend">
                <fmt:message key="${validationResult.getMessage(RequestParameter.NAME)}" />
            </div>
        </c:if>
        <div class="invalid-feedback">
            <fmt:message key="label.wrong-input" />
        </div>
    </div>
    <div class="form-group input-group">
        <input type="color" id="color" name="color" value="${!empty validationResult.getValue(RequestParameter.COLOR) ? validationResult.getValue(RequestParameter.COLOR) : category.color}">&nbsp;&nbsp;
        <label for="color"> <fmt:message key="label.category.color" /></label>
        <c:if test="${!empty validationResult.getMessage(RequestParameter.COLOR)}">
            <div class="invalid-feedback-backend">
                <fmt:message key="${validationResult.getMessage(RequestParameter.COLOR)}" />
            </div>
        </c:if>
        <div class="invalid-feedback">
            <fmt:message key="label.wrong-input" />
        </div>
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary btn-block">
            <fmt:message key="label.save.button" />
        </button>
    </div>
</form>
<jsp:include page="layout/footer.jsp" />