<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ft" uri="formtags" %>
<jsp:include page="layout/header.jsp" />
<div class="col-lg-7 mx-auto">
    <form id="login_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=login" novalidate>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-user"></i> </span>
            </div>
            <input name="login" class="form-control<ft:field-class-detector status="${validationResult.getField(RequestParameter.LOGIN).status}" />"
                   placeholder="Login" type="text" required pattern="[A-Za-z0-9]{1,25}" maxlength="20"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="Логин может содержать только латинские буквы в любом регистре и цифры, максимальная длина 20 символов"
                   value="${validationResult.getField(RequestParameter.LOGIN).value}"
            >
            <div class="invalid-feedback-backend">
                ${validationResult.getField(RequestParameter.LOGIN).message}
            </div>
            <div class="invalid-feedback">
                Заполните поле корректно
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
            </div>
            <input class="form-control<ft:field-class-detector status="${validationResult.getField(RequestParameter.PASSWORD).status}" />"
                   placeholder="Password" type="password" name="password" id="password" required pattern="(?=.*[0-9])(?=.*[A-ZА-Я])\S{6,}" maxlength="25"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="Пароль должен содержать от 6 до 25 символов, хотя бы 1 цифру и 1 заглавную букву"
                   value="${validationResult.getField(RequestParameter.PASSWORD).value}"
            >
            <div class="invalid-feedback-backend">
                ${validationResult.getField(RequestParameter.PASSWORD).message}
            </div>
            <div class="invalid-feedback">
                Заполните поле корректно
            </div>
        </div>
        <div class="form-group form-check">
            <label class="switch">
                <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="rememberMe" id="rememberMe" checked>
                <span class="slider round"></span>
            </label>
            <label class="form-check-label" for="rememberMe">Запомнить меня</label>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block"> Войти  </button>
        </div>
    </form>
</div>
<script>
    $('#login_form').on('input', function () {
        var password = document.getElementById("password");
        var password_confirmed = document.getElementById("password_confirmed");

        if(password.value != password_confirmed.value) {
            password_confirmed.setCustomValidity("Please repeat password correctly");
        } else {
            password_confirmed.setCustomValidity("");
        }
    });
</script>
<jsp:include page="layout/footer.jsp" />