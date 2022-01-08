<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
                 by.latushko.anyqueries.validator.ValidationPattern" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="page_title_label" value="label.profile" scope="request" />
<jsp:include page="layout/header.jsp" />
<div class="row">
    <jsp:include page="fragment/editProfileLeftColumn.jsp" />
    <div class="col-sm-9" id="profile-nav">
        <ul class="nav nav-tabs">
            <li class="nav-item">
                <a class="nav-link active" id="pills-home-tab" href="${pageContext.request.contextPath}/controller?command=edit_profile_page" role="tab" aria-selected="true">
                    <fmt:message key="label.personalData" />
                </a>
            </li>
            <li class="nav-item" role="presentation">
                <a class="nav-link" id="pills-profile-tab" href="${pageContext.request.contextPath}/controller?command=change_password_page" role="tab" aria-selected="false">
                    <fmt:message key="label.changePassword" />
                </a>
            </li>
        </ul>
        <div class="tab-content pills-tab" id="pills-tabContent">
            <div class="tab-pane fade show active" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab">
                <form id="edit_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=edit_profile" novalidate autocomplete="off">
                    <div class="form-group input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
                        </div>
                        <input name="first_name" class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.FIRST_NAME)}" />"
                               placeholder="<fmt:message key="label.firstName.placeholder" />" type="text" required pattern="${ValidationPattern.FIRST_NAME_REGEXP}" maxlength="25"
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.firstName.info" />"
                               value="${!empty validationResult.getField(RequestParameter.FIRST_NAME) ? validationResult.getValue(RequestParameter.FIRST_NAME) : principal.firstName}"
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
                        <input name="last_name" class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.LAST_NAME)}" />"
                               placeholder="<fmt:message key="label.lastName.placeholder" />" type="text" required pattern="${ValidationPattern.LAST_NAME_REGEXP}" maxlength="25"
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.lastName.info" />"
                               value="${!empty validationResult.getField(RequestParameter.LAST_NAME) ? validationResult.getValue(RequestParameter.LAST_NAME) : principal.lastName}"
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
                        <input name="middle_name" class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.MIDDLE_NAME)}" />"
                               placeholder="<fmt:message key="label.middleName.placeholder" />" type="text" required pattern="${ValidationPattern.MIDDLE_NAME_REGEXP}" maxlength="25"
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.middleName.info" />"
                               value="${!empty validationResult.getField(RequestParameter.MIDDLE_NAME) ? validationResult.getValue(RequestParameter.MIDDLE_NAME) : principal.middleName}"
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
                        <input name="email" id="email"
                               class="form-control input-confirmation<at:field-class-detector field="${validationResult.getField(RequestParameter.EMAIL)}" />"
                               placeholder="<fmt:message key="label.email.placeholder" />" type="email"
                               pattern="${ValidationPattern.EMAIL_REGEXP}" maxlength="100"
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.email.info" />"
                               value="${!empty validationResult.getField(RequestParameter.EMAIL) ? validationResult.getValue(RequestParameter.EMAIL) : principal.email}"
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
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.telegram.info" />"
                               value="${!empty validationResult.getField(RequestParameter.TELEGRAM) ? validationResult.getValue(RequestParameter.TELEGRAM) : principal.telegram}"
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
                    <div class="form-group input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
                        </div>
                        <input name="login" class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.LOGIN)}" />"
                               placeholder="<fmt:message key="label.login.placeholder" />" type="text" required pattern="${ValidationPattern.LOGIN_REGEXP}" maxlength="20"
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.login.info" />"
                               value="${!empty validationResult.getField(RequestParameter.LOGIN) ? validationResult.getValue(RequestParameter.LOGIN) : principal.login}"
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
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary btn-block" id="btnSubmit">
                            <fmt:message key="label.save.button" />
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="layout/footer.jsp" />