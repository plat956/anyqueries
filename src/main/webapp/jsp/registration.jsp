<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="page_title" value="Регистрация" scope="request" />
<jsp:include page="layout/header.jsp" />

<div class="col-lg-7 mx-auto">
<form id="reg_form" class="needs-validation" method="post" action="/controller?command=registration" novalidate>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
        </div>
        <input name="first_name" class="form-control" placeholder="First name" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="Имя может состоять только из букв в любом регистре, максимальная длина 25 символов">
        <div class="invalid-feedback">
            Заполните поле корректно
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
        </div>
        <input name="last_name" class="form-control" placeholder="Last name" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="Фамилия может состоять только из букв в любом регистре, максимальная длина 25 символов">
        <div class="invalid-feedback">
            Заполните поле корректно
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
        </div>
        <input name="middle_name" class="form-control" placeholder="Middle name" type="text" required pattern="[A-Za-zА-Яа-я]{1,25}" maxlength="25"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="Отчество может состоять только из букв в любом регистре, максимальная длина 25 символов">
        <div class="invalid-feedback">
            Заполните поле корректно
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-envelope"></i> </span>
        </div>
        <input name="email" class="form-control" placeholder="Email address" type="email" required pattern="[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,25}$" maxlength="100"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="Пример: user@mail.com, максимальная длина 100 символов">
        <div class="invalid-feedback">
            Заполните поле корректно
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-user"></i> </span>
        </div>
        <input name="login" class="form-control" placeholder="Login" type="text" required pattern="[A-Za-z0-9]{1,25}" maxlength="20"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="Логин может содержать только латинские буквы в любом регистре и цифры, максимальная длина 20 символов">
        <div class="invalid-feedback">
            Заполните поле корректно
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
        </div>
        <input class="form-control" placeholder="Password" type="password" name="password" id="password" required pattern="(?=.*[0-9])(?=.*[A-ZА-Я])\S{6,}" maxlength="25"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="Пароль должен содержать от 6 до 25 символов, хотя бы 1 цифру и 1 заглавную букву">
        <div class="invalid-feedback">
            Заполните поле корректно
        </div>
    </div>
    <div class="form-group input-group">
        <div class="input-group-prepend">
            <span class="input-group-text"> <i class="fa fa-lock"></i> </span>
        </div>
        <input class="form-control" placeholder="Repeat password" type="password" name="password_confirmed" id="password_confirmed" required pattern="(?=.*[0-9])(?=.*[A-ZА-Я])\S{6,}" maxlength="25"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="Повторите введенный выше пароль">
        <div class="invalid-feedback">
            Заполните поле корректно
        </div>
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary btn-block" id="btnSubmit"> Зарегистрироваться  </button>
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