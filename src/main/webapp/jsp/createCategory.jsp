<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
         by.latushko.anyqueries.validator.ValidationPattern" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="label.createCategory" scope="request" />
<jsp:include page="layout/header.jsp" />
<form id="create_form" class="needs-validation" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/controller?command=create_category" novalidate autocomplete="off">
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-folder-open" aria-hidden="true"></i> </span>
        </div>
        <input name="name" class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.NAME)}" />"
               placeholder="<fmt:message key="label.category.name" />" type="text" required pattern="${ValidationPattern.CATEGORY_NAME_REGEXP}" maxlength="25"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="<fmt:message key="label.category.name.info" />"
               value="${validationResult.getValue(RequestParameter.NAME)}"
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
        <input type="color" id="color" name="color" value="#000">&nbsp;&nbsp;
        <label for="color"> <fmt:message key="label.category.color" /></label>
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary btn-block">
            <fmt:message key="label.create.button" />
        </button>
    </div>
</form>
<jsp:include page="layout/footer.jsp" />