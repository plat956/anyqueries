<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="label.repeatActivation" scope="request" />
<jsp:include page="layout/header.jsp" />
<div class="col-lg-7 mx-auto">
    <form id="reg_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=repeat_activation" novalidate>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-envelope"></i> </span>
            </div>
            <input name="email" id="email" class="form-control input-confirmation" placeholder="<fmt:message key="label.email.placeholder" />" type="email" pattern="[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,25}$" maxlength="100" required
                   value="${inactivePrincipal.email}"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.email.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fab fa-telegram"></i> </span>
            </div>
            <input name="telegram" id="telegram" class="form-control input-confirmation" placeholder="<fmt:message key="label.telegram.placeholder" />" type="text" pattern="[a-z0-9_]{5,32}" maxlength="32"
                   value="${inactivePrincipal.telegram}"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.telegram.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group form-check">
            <label class="switch">
                <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="send_link" id="sendLink" onchange="registration.sendLink(this)" >
                <span class="slider round"></span>
            </label>
            <label class="form-check-label" for="sendLink"><fmt:message key="label.registration.sendConfirmation" /></label>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block" id="btnSubmit">
                Обновить регистрационные данные
            </button>
        </div>
    </form>
</div>
<jsp:include page="layout/footer.jsp" />