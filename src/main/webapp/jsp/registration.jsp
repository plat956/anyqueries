<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
         by.latushko.anyqueries.validator.ValidationPattern" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ft" uri="formtags" %>
<c:set var="page_title_label" value="label.registration" scope="request" />
<jsp:include page="layout/header.jsp" />
<div class="col-lg-7 mx-auto">
    <form id="reg_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=registration" novalidate>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="first_name" class="form-control<ft:field-class-detector field="${validationResult.getField(RequestParameter.FIRST_NAME)}" />"
                   placeholder="<fmt:message key="label.firstName.placeholder" />" type="text" required pattern="${ValidationPattern.FIRST_NAME_REGEXP}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.firstName.info" />"
                   value="${validationResult.getValue(RequestParameter.FIRST_NAME)}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.FIRST_NAME)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.FIRST_NAME)}" />
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="last_name" class="form-control<ft:field-class-detector field="${validationResult.getField(RequestParameter.LAST_NAME)}" />"
                   placeholder="<fmt:message key="label.lastName.placeholder" />" type="text" required pattern="${ValidationPattern.LAST_NAME_REGEXP}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.lastName.info" />"
                   value="${validationResult.getValue(RequestParameter.LAST_NAME)}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.LAST_NAME)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.LAST_NAME)}" />
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="middle_name" class="form-control<ft:field-class-detector field="${validationResult.getField(RequestParameter.MIDDLE_NAME)}" />"
                   placeholder="<fmt:message key="label.middleName.placeholder" />" type="text" required pattern="${ValidationPattern.MIDDLE_NAME_REGEXP}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.middleName.info" />"
                   value="${validationResult.getValue(RequestParameter.MIDDLE_NAME)}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.MIDDLE_NAME)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.MIDDLE_NAME)}" />
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-envelope"></i> </span>
            </div>
            <input name="email" id="email" class="form-control input-confirmation<ft:field-class-detector field="${validationResult.getField(RequestParameter.EMAIL)}" />"
                   placeholder="<fmt:message key="label.email.placeholder" />" type="email" pattern="${ValidationPattern.EMAIL_REGEXP}" maxlength="100"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.email.info" />"
                   value="${validationResult.getValue(RequestParameter.EMAIL)}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.EMAIL)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.EMAIL)}" />
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fab fa-telegram"></i> </span>
            </div>
            <input name="telegram" id="telegram" class="form-control input-confirmation<ft:field-class-detector field="${validationResult.getField(RequestParameter.TELEGRAM)}" />"
                   placeholder="<fmt:message key="label.telegram.placeholder" />" type="text" pattern="${ValidationPattern.TELEGRAM_REGEXP}" maxlength="32" required
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.telegram.info" />"
                   value="${validationResult.getValue(RequestParameter.TELEGRAM)}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.TELEGRAM)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.TELEGRAM)}" />
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="login" class="form-control<ft:field-class-detector field="${validationResult.getField(RequestParameter.LOGIN)}" />"
                   placeholder="<fmt:message key="label.login.placeholder" />" type="text" required pattern="${ValidationPattern.LOGIN_REGEXP}" maxlength="20"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.login.info" />"
                   value="${validationResult.getValue(RequestParameter.LOGIN)}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.LOGIN)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.LOGIN)}" />
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input class="form-control<ft:field-class-detector field="${validationResult.getField(RequestParameter.PASSWORD)}" />"
                   placeholder="<fmt:message key="label.password.placeholder" />" type="password" name="password" id="password" required
                   pattern="${ValidationPattern.PASSWORD_REGEXP}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.password.info" />"
                   value="${validationResult.getValue(RequestParameter.PASSWORD)}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.PASSWORD)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.PASSWORD)}" />
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input class="form-control<ft:field-class-detector field="${validationResult.getField(RequestParameter.PASSWORD_CONFIRMED)}" />"
                   placeholder="<fmt:message key="label.passwordConfirm.placeholder" />" type="password" name="password_confirmed" id="password_confirmed" required
                   pattern="${ValidationPattern.PASSWORD_REGEXP}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.passwordConfirm.info" />"
                   value="${validationResult.getValue(RequestParameter.PASSWORD_CONFIRMED)}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.PASSWORD_CONFIRMED)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.PASSWORD_CONFIRMED)}" />
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group form-check">
            <label class="switch">
                <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="send_link" id="sendLink" onchange="registration.sendLink(this)"
                <c:if test="${validationResult.containsField(RequestParameter.SEND_LINK)}">
                       checked
                </c:if>
                >
                <span class="slider round"></span>
            </label>
            <label class="form-check-label" for="sendLink"><fmt:message key="label.registration.sendConfirmation" /></label>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block" id="btnSubmit">
                <fmt:message key="label.registration.button" />
            </button>
        </div>
    </form>
</div>
<script type="text/javascript">
    //custom password check
    $('#reg_form').on('input', function () {
        var password = document.getElementById("password");
        var password_confirmed = document.getElementById("password_confirmed");

        if(password.value != password_confirmed.value) {
            password_confirmed.setCustomValidity("error");
        } else {
            password_confirmed.setCustomValidity("");
        }
    });
</script>
<jsp:include page="layout/footer.jsp" />