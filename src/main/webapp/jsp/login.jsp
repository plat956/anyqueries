<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ft" uri="formtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="label.login" scope="request" />
<jsp:include page="layout/header.jsp" />
<div class="col-lg-7 mx-auto">
    <form id="login_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=login" novalidate>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="login" class="form-control<ft:field-class-detector status="${validationResult.getField(RequestParameter.LOGIN).status}" />"
                   placeholder="<fmt:message key="label.login.placeholder" />" type="text" required pattern="[A-Za-z0-9]{1,25}" maxlength="20"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.login.info" />"
                   value="${validationResult.getField(RequestParameter.LOGIN).value}"
            >
            <div class="invalid-feedback-backend">
                ${validationResult.getField(RequestParameter.LOGIN).message}
            </div>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input class="form-control<ft:field-class-detector status="${validationResult.getField(RequestParameter.PASSWORD).status}" />"
                   placeholder="<fmt:message key="label.password.placeholder" />" type="password" name="password" id="password" required pattern="(?=.*[0-9])(?=.*[A-ZА-Я])\S{6,}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.password.info" />"
                   value="${validationResult.getField(RequestParameter.PASSWORD).value}"
            >
            <div class="invalid-feedback-backend">
                ${validationResult.getField(RequestParameter.PASSWORD).message}
            </div>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group form-check">
            <label class="switch">
                <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="rememberMe" id="rememberMe" checked>
                <span class="slider round"></span>
            </label>
            <label class="form-check-label" for="rememberMe"><fmt:message key="label.rememberMe" /></label>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block">
                <fmt:message key="label.login.button" />
            </button>
        </div>
    </form>
</div>
<jsp:include page="layout/footer.jsp" />