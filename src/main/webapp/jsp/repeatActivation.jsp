<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
                 by.latushko.anyqueries.validator.ValidationPattern" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="at" uri="apptags" %>
<c:set var="page_title_label" value="label.repeatActivation" scope="request"/>
<jsp:include page="layout/header.jsp"/>
<div class="col-lg-7 mx-auto">
    <form id="repeat_form" class="needs-validation" method="post"
          action="${pageContext.request.contextPath}/controller?command=repeat_activation" novalidate autocomplete="off">
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-envelope"></i> </span>
            </div>
            <input name="email" id="email"
                   class="form-control input-confirmation<at:field-class-detector field="${validationResult.getField(RequestParameter.EMAIL)}" />"
                   placeholder="<fmt:message key="label.email.placeholder" />" type="email"
                   pattern="${ValidationPattern.EMAIL_REGEXP}" maxlength="100"
            <c:if test="${validationResult.containsField(RequestParameter.SEND_LINK)}">
                   required
            </c:if>
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.email.info" />"
                   value="${!empty validationResult.getField(RequestParameter.EMAIL) ? validationResult.getValue(RequestParameter.EMAIL) : inactivePrincipal.email}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.EMAIL)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.EMAIL)}"/>
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input"/>
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fab fa-telegram"></i> </span>
            </div>
            <input name="telegram" id="telegram"
                   class="form-control input-confirmation<at:field-class-detector field="${validationResult.getField(RequestParameter.TELEGRAM)}" />"
                   placeholder="<fmt:message key="label.telegram.placeholder" />" type="text"
                   pattern="${ValidationPattern.TELEGRAM_REGEXP}" maxlength="32"
            <c:if test="${!validationResult.containsField(RequestParameter.SEND_LINK)}">
                   required
            </c:if>
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.telegram.info" />"
                   value="${!empty validationResult.getField(RequestParameter.TELEGRAM) ? validationResult.getValue(RequestParameter.TELEGRAM) : inactivePrincipal.telegram}"
            >
            <c:if test="${!empty validationResult.getMessage(RequestParameter.TELEGRAM)}">
                <div class="invalid-feedback-backend">
                    <fmt:message key="${validationResult.getMessage(RequestParameter.TELEGRAM)}"/>
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input"/>
            </div>
        </div>
        <div class="form-group form-check">
            <label class="switch">
                <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="send_link"
                       id="sendLink" onchange="registration.sendLink(this)"
                <c:if test="${validationResult.containsField(RequestParameter.SEND_LINK)}">
                       checked
                </c:if>
                >
                <span class="slider round"></span>
            </label>
            <label class="form-check-label" for="sendLink"><fmt:message
                    key="label.registration.sendConfirmation"/></label>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block" id="btnSubmit">
                <fmt:message key="label.repeatActivation.button"/>
            </button>
        </div>
    </form>
</div>
<jsp:include page="layout/footer.jsp"/>