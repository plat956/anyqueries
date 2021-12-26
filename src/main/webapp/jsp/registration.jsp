<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="label.registration" scope="request" />
<jsp:include page="layout/header.jsp" />
<div class="reg-confirm-block">
    <div id="telegram-confirmation" class="notice-confirmation alert alert-warning" style="display: none">
        <fmt:message key="message.telegram.warning.part1" /> <a href="https://t.me/${telegramBot}" target="_blank"><strong>@${telegramBot}</strong></a>. <fmt:message key="message.telegram.warning.part2" /> ${initParam['website']} <fmt:message key="message.telegram.warning.part3" /> "${activationCommand}"
    </div>
    <div id="email-confirmation" class="notice-confirmation alert alert-warning" style="display: none">
        <fmt:message key="message.email.warning" />
    </div>
</div>
<div class="col-lg-7 mx-auto">
    <form id="reg_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=registration" novalidate>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="first_name" class="form-control" placeholder="<fmt:message key="label.firstName.placeholder" />" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.firstName.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="last_name" class="form-control" placeholder="<fmt:message key="label.lastName.placeholder" />" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.lastName.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="middle_name" class="form-control" placeholder="<fmt:message key="label.middleName.placeholder" />" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.middleName.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <select name="confirmation_type" class="form-control" required onchange="registration.chooseMode(this.value)">
                <option value="" selected disabled data-content='<i class="fa fa-check-square-o"></i> <fmt:message key="label.confirmationType.placeholder" />'><fmt:message key="label.confirmationType.placeholder" /></option>
                <option value="telegram" data-content='<i class="fab fa-telegram"></i> <fmt:message key="label.telegramBot" />'><fmt:message key="label.telegramBot" /></option>
                <option value="email" data-content='<i class="fa fa-envelope"></i> Email'><fmt:message key="label.email" /></option>
            </select>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-envelope"></i> </span>
            </div>
            <input name="email" id="email" class="form-control input-confirmation" placeholder="<fmt:message key="label.email.placeholder" />" type="email" pattern="[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,25}$" maxlength="100"
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
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.telegram.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="login" class="form-control" placeholder="<fmt:message key="label.login.placeholder" />" type="text" required pattern="[A-Za-z0-9]{1,25}" maxlength="20"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.login.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input class="form-control" placeholder="<fmt:message key="label.password.placeholder" />" type="password" name="password" id="password" required pattern="(?=.*[0-9])(?=.*[A-ZА-Я])\S{6,}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.password.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input class="form-control" placeholder="<fmt:message key="label.passwordConfirm.placeholder" />" type="password" name="password_confirmed" id="password_confirmed" required pattern="(?=.*[0-9])(?=.*[A-ZА-Я])\S{6,}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="<fmt:message key="label.passwordConfirm.info" />">
            <div class="invalid-feedback">
                <fmt:message key="label.wrong-input" />
            </div>
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