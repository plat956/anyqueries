<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="page_title" value="Регистрация" scope="request" />
<jsp:include page="layout/header.jsp" />
<div class="col-lg-7 mx-auto">
<form id="reg_form" class="needs-validation" novalidate>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
        </div>
        <input name="first_name" class="form-control" placeholder="First name" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25">
        <div class="invalid-feedback">
            Имя может состоять только из букв, макс. 25 символов.
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
        </div>
        <input name="last_name" class="form-control" placeholder="Last name" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25">
        <div class="invalid-feedback">
            Фамилия может состоять только из букв, макс. 25 символов.
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
        </div>
        <input name="middle_name" class="form-control" placeholder="Middle name" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25">
        <div class="invalid-feedback">
            Отчество может состоять только из букв, макс. 25 символов.
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-envelope"></i> </span>
        </div>
        <input name="email" class="form-control" placeholder="Email address" type="email" required pattern="[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,25}$" maxlength="100">
        <div class="invalid-feedback">
            Введите корректный email (Пример user@domain.by)
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
        </div>
        <input name="login" class="form-control" placeholder="Login" type="text" required pattern="[A-Za-z0-9]{1,25}" maxlength="20">
        <div class="invalid-feedback">
            Для логина допустимы только латинские буквы и цифры, макс. 20 символов.
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
        </div>
        <input class="form-control" placeholder="Password" type="password" name="password" id="password" required pattern="(?=.*[0-9])(?=.*[A-ZА-Я])\S{6,}" maxlength="25">
        <div class="invalid-feedback">
            Длина пароля дожна быть 6-25 символов, мин. 1 цифра, 1 заглавная буква.
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
        </div>
        <input class="form-control" placeholder="Repeat password" type="password" name="password_confirmed" id="password_confirmed" required pattern="(?=.*[0-9])(?=.*[A-ZА-Я])\S{6,}" maxlength="25">
        <div class="invalid-feedback">
            Повторите пароль
        </div>
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary btn-block"> Зарегистрироваться  </button>
    </div>
</form>
</div>
<script>
    (function() {
        'use strict';
        window.addEventListener('load', function() {
            var forms = document.getElementsByClassName('needs-validation');
            var validation = Array.prototype.filter.call(forms, function(form) {
                form.addEventListener('submit', function(event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        }, false);
    })();

    $('#reg_form').on('input', function () {
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