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
                <a class="nav-link" id="pills-home-tab" href="${pageContext.request.contextPath}/controller?command=edit_profile_page" role="tab" aria-selected="true">
                    <fmt:message key="label.personalData" />
                </a>
            </li>
            <li class="nav-item" role="presentation">
                <a class="nav-link active" id="pills-profile-tab" href="${pageContext.request.contextPath}/controller?command=change_password_page" role="tab" aria-selected="false">
                    <fmt:message key="label.changePassword" />
                </a>
            </li>
        </ul>
        <div class="tab-content pills-tab" id="pills-tabContent">
            <div class="tab-pane fade show active" id="pills-profile" role="tabpanel" aria-labelledby="pills-profile-tab">
                <form id="update_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=change_password" novalidate autocomplete="off">
                    <div class="form-group input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
                        </div>
                        <input class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.PASSWORD_OLD)}" />"
                               placeholder="<fmt:message key="label.passwordOld.placeholder" />" type="password" name="password_old" id="password_old" required
                               pattern="${ValidationPattern.PASSWORD_REGEXP}" maxlength="25"
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.password.info" />"
                               value="${validationResult.getValue(RequestParameter.PASSWORD_OLD)}"
                        >
                        <c:if test="${!empty validationResult.getMessage(RequestParameter.PASSWORD_OLD)}">
                            <div class="invalid-feedback-backend">
                                <fmt:message key="${validationResult.getMessage(RequestParameter.PASSWORD_OLD)}" />
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
                        <input class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.PASSWORD_NEW)}" />"
                               placeholder="<fmt:message key="label.passwordNew.placeholder" />" type="password" name="password_new" id="password_new" required
                               pattern="${ValidationPattern.PASSWORD_REGEXP}" maxlength="25"
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.password.info" />"
                               value="${validationResult.getValue(RequestParameter.PASSWORD_NEW)}"
                        >
                        <c:if test="${!empty validationResult.getMessage(RequestParameter.PASSWORD_NEW)}">
                            <div class="invalid-feedback-backend">
                                <fmt:message key="${validationResult.getMessage(RequestParameter.PASSWORD_NEW)}" />
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
                        <input class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.PASSWORD_NEW_REPEAT)}" />"
                               placeholder="<fmt:message key="label.passwordNew.repeat.placeholder" />" type="password" name="password_new_repeat" id="password_new_repeat" required
                               pattern="${ValidationPattern.PASSWORD_REGEXP}" maxlength="25"
                               data-toggle="popover" data-trigger="focus" data-placement="right"
                               data-content="<fmt:message key="label.passwordConfirm.info" />"
                               value="${validationResult.getValue(RequestParameter.PASSWORD_NEW_REPEAT)}"
                        >
                        <c:if test="${!empty validationResult.getMessage(RequestParameter.PASSWORD_NEW_REPEAT)}">
                            <div class="invalid-feedback-backend">
                                <fmt:message key="${validationResult.getMessage(RequestParameter.PASSWORD_NEW_REPEAT)}" />
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
<script type="text/javascript">
    //custom password check
    $('#update_form').on('input', function () {
        var password = document.getElementById("password_new");
        var password_confirmed = document.getElementById("password_new_repeat");

        if(password.value != password_confirmed.value) {
            password_confirmed.setCustomValidity("error");
        } else {
            password_confirmed.setCustomValidity("");
        }
    });
</script>
<jsp:include page="layout/footer.jsp" />