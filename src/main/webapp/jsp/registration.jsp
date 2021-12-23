<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="label.registration" scope="request" />
<jsp:include page="layout/header.jsp" />
<div class="reg-confirm-block">
    <div id="telegram-confirmation" class="notice-confirmation alert alert-warning" style="display: none">
        Чтобы активировать учетную запись подпишитесь на telegram-бота <a href="https://t.me/${telegramBot}" target="_blank"><strong>@${telegramBot}</strong></a>. После регистрации на ${initParam['website']} отправьте боту команду "${activationCommand}"
    </div>
    <div id="email-confirmation" class="notice-confirmation alert alert-warning" style="display: none">
        На ваш email будет направлена ссылка для активации учетной записи
    </div>
</div>
<div class="col-lg-7 mx-auto">
    <form id="reg_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=registration" novalidate>
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
            <select name="confirmation_type" class="form-control" required onchange="registration.chooseMode(this.value)">
                <option value="" selected disabled data-content='<i class="fa fa-check-square-o"></i> Choose account activation mode'>Choose account activation mode</option>
                <option value="telegram" data-content='<i class="fab fa-telegram"></i> Telegram bot'>Telegram bot</option>
                <option value="email" data-content='<i class="fa fa-envelope"></i> Email'>Email</option>
            </select>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fa fa-envelope"></i> </span>
            </div>
            <input name="email" id="email" class="form-control input-confirmation" placeholder="Email address" type="email" pattern="[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,25}$" maxlength="100"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="Пример: user@mail.com, максимальная длина 100 символов">
            <div class="invalid-feedback">
                Заполните поле корректно
            </div>
        </div>
        <div class="form-group input-group">
            <div class="input-group-prepend">
                <span class="input-group-text"> <i class="fab fa-telegram"></i> </span>
            </div>
            <input name="telegram" id="telegram" class="form-control input-confirmation" placeholder="Telegram account" type="text" pattern="[a-z0-9_]{5,32}" maxlength="32"
                   data-toggle="popover" data-trigger="focus" data-placement="right"
                   data-content="Допустимые символы a-z, 0-9, _, длина от 5 до 32 символов">
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