<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
                 by.latushko.anyqueries.validator.ValidationPattern" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="page_title_label" value="label.profile.edit" scope="request"/>
<jsp:include page="layout/header.jsp"/>
<div class="row">
    <div class="col-sm-12">
        <form id="edit_form" class="needs-validation" method="post"
              action="${pageContext.request.contextPath}/controller?command=edit_user" novalidate autocomplete="off">
            <input type="hidden" name="id" value="${user.id}" />
            <input type="hidden" name="previousPage" value="${empty previousPage ? header.referer : previousPage}">
            <div class="form-group input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text"> <i class="fa fa-user"></i> </span>
                </div>
                <input name="first_name"
                       class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.FIRST_NAME)}" />"
                       placeholder="<fmt:message key="label.firstName.placeholder" />" type="text" required
                       pattern="${ValidationPattern.FIRST_NAME_REGEXP}" maxlength="25"
                       data-toggle="popover" data-trigger="focus" data-placement="right"
                       data-content="<fmt:message key="label.firstName.info" />"
                       value="${!empty validationResult.getField(RequestParameter.FIRST_NAME) ? validationResult.getValue(RequestParameter.FIRST_NAME) : user.firstName}"
                >
                <c:if test="${!empty validationResult.getMessage(RequestParameter.FIRST_NAME)}">
                    <div class="invalid-feedback-backend">
                        <fmt:message key="${validationResult.getMessage(RequestParameter.FIRST_NAME)}"/>
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
                <input name="last_name"
                       class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.LAST_NAME)}" />"
                       placeholder="<fmt:message key="label.lastName.placeholder" />" type="text" required
                       pattern="${ValidationPattern.LAST_NAME_REGEXP}" maxlength="25"
                       data-toggle="popover" data-trigger="focus" data-placement="right"
                       data-content="<fmt:message key="label.lastName.info" />"
                       value="${!empty validationResult.getField(RequestParameter.LAST_NAME) ? validationResult.getValue(RequestParameter.LAST_NAME) : user.lastName}"
                >
                <c:if test="${!empty validationResult.getMessage(RequestParameter.LAST_NAME)}">
                    <div class="invalid-feedback-backend">
                        <fmt:message key="${validationResult.getMessage(RequestParameter.LAST_NAME)}"/>
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
                <input name="middle_name"
                       class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.MIDDLE_NAME)}" />"
                       placeholder="<fmt:message key="label.middleName.placeholder" />" type="text" required
                       pattern="${ValidationPattern.MIDDLE_NAME_REGEXP}" maxlength="25"
                       data-toggle="popover" data-trigger="focus" data-placement="right"
                       data-content="<fmt:message key="label.middleName.info" />"
                       value="${!empty validationResult.getField(RequestParameter.MIDDLE_NAME) ? validationResult.getValue(RequestParameter.MIDDLE_NAME) : user.middleName}"
                >
                <c:if test="${!empty validationResult.getMessage(RequestParameter.MIDDLE_NAME)}">
                    <div class="invalid-feedback-backend">
                        <fmt:message key="${validationResult.getMessage(RequestParameter.MIDDLE_NAME)}"/>
                    </div>
                </c:if>
                <div class="invalid-feedback">
                    <fmt:message key="label.wrong-input"/>
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
                       value="${!empty validationResult.getField(RequestParameter.EMAIL) ? validationResult.getValue(RequestParameter.EMAIL) : user.email}"
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
                       value="${!empty validationResult.getField(RequestParameter.TELEGRAM) ? validationResult.getValue(RequestParameter.TELEGRAM) : user.telegram}"
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
                <input name="login"
                       class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.LOGIN)}" />"
                       placeholder="<fmt:message key="label.login.placeholder" />" type="text" required
                       pattern="${ValidationPattern.LOGIN_REGEXP}" maxlength="20"
                       data-toggle="popover" data-trigger="focus" data-placement="right"
                       data-content="<fmt:message key="label.login.info" />"
                       value="${!empty validationResult.getField(RequestParameter.LOGIN) ? validationResult.getValue(RequestParameter.LOGIN) : user.login}"
                >
                <c:if test="${!empty validationResult.getMessage(RequestParameter.LOGIN)}">
                    <div class="invalid-feedback-backend">
                        <fmt:message key="${validationResult.getMessage(RequestParameter.LOGIN)}"/>
                    </div>
                </c:if>
                <div class="invalid-feedback">
                    <fmt:message key="label.wrong-input"/>
                </div>
            </div>

            <div class="form-group">
                <select class="selectpicker${!empty validationResult.getMessage(RequestParameter.STATUS) ? ' error-invalid' : ''}" data-width="100%" name="status" required>
                    <option value="" selected disabled><fmt:message key="label.status.placeholder" /></option>
                    <c:set var="selected_status" value="${!empty validationResult ? validationResult.getValue(RequestParameter.STATUS) : user.status}" />
                    <c:forEach var="c" items="${statuses}">
                        <option value="${c}"${selected_status == c ? ' selected' : ''} data-content='<span class="badge badge-${c == 'ACTIVE' ? 'success' : (c == 'INACTIVE' ? 'secondary' : 'danger')} user-role-span"><fmt:message key="label.status.${fn:toLowerCase(c)}" /></span>'>
                            <span class="badge badge-${c == 'ACTIVE' ? 'success' : (c == 'INACTIVE' ? 'secondary' : 'danger')} user-role-span"><fmt:message key="label.status.${fn:toLowerCase(c)}" /></span>
                        </option>
                    </c:forEach>
                </select>
                <c:if test="${!empty validationResult.getMessage(RequestParameter.STATUS)}">
                    <div class="invalid-feedback-backend">
                        <fmt:message key="${validationResult.getMessage(RequestParameter.STATUS)}" />
                    </div>
                </c:if>
                <div class="invalid-feedback">
                    <fmt:message key="label.wrong-input" />
                </div>
            </div>

            <div class="form-group">
                <select class="selectpicker${!empty validationResult.getMessage(RequestParameter.ROLE) ? ' error-invalid' : ''}" data-width="100%" name="role" required>
                    <option value="" selected disabled><fmt:message key="label.status.placeholder" /></option>
                    <c:set var="selected_role" value="${!empty validationResult ? validationResult.getValue(RequestParameter.ROLE) : user.role}" />
                    <c:forEach var="c" items="${roles}">
                        <option value="${c}"${selected_role == c ? ' selected' : ''} data-content='<span class="badge badge-${c.color} user-role-span"><fmt:message key="label.role.${fn:toLowerCase(c)}" /></span>'>
                            <span class="badge badge-${c.color} user-role-span"><fmt:message key="label.role.${fn:toLowerCase(c)}" /></span>
                        </option>
                    </c:forEach>
                </select>
                <c:if test="${!empty validationResult.getMessage(RequestParameter.ROLE)}">
                    <div class="invalid-feedback-backend">
                        <fmt:message key="${validationResult.getMessage(RequestParameter.ROLE)}" />
                    </div>
                </c:if>
                <div class="invalid-feedback">
                    <fmt:message key="label.wrong-input" />
                </div>
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary btn-block" id="btnSubmit">
                    <fmt:message key="label.save.button"/>
                </button>
            </div>
        </form>
    </div>
</div>
<jsp:include page="layout/footer.jsp"/>