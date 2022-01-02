<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
         by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ft" uri="formtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="label.createQuestion" scope="request" />
<jsp:include page="layout/header.jsp" />
<form id="login_form" class="needs-validation" method="post" action="${pageContext.request.contextPath}/controller?command=create_question" novalidate>
    <div class="form-group">
        <select class="selectpicker" data-width="100%" name="category">
            <option value="">1</option>
        </select>
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
        <input name="login" class="form-control<ft:field-class-detector field="${validationResult.getField(RequestParameter.LOGIN)}" />"
               placeholder="<fmt:message key="label.questionTitle.placeholder" />" type="text" required pattern="${ValidationPattern.LOGIN_REGEXP}" maxlength="20"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="todo <fmt:message key="label.questionTitle.placeholder" />"
               value="${validationResult.getValue(RequestParameter.LOGIN)}"
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
        <textarea id="text" name="text" class="summernote form-control<ft:field-class-detector field="${validationResult.getField(RequestParameter.LOGIN)}" />"
                  placeholder="<fmt:message key="label.login.placeholder" />" required maxlength="${AppProperty.APP_TEXTAREA_MAXLENGTH}"></textarea>
        <c:if test="${!empty validationResult.getMessage(RequestParameter.LOGIN)}">
            <div class="invalid-feedback-backend">
                <fmt:message key="${validationResult.getMessage(RequestParameter.LOGIN)}" />
            </div>
        </c:if>
        <div class="invalid-feedback">
            <fmt:message key="label.wrong-input" />
        </div>
        <fmt:message key="label.textarea.remaining" /> <span id="text-counter">${AppProperty.APP_TEXTAREA_MAXLENGTH}</span>
    </div>

    <div class="form-group">
        <div class="alert alert-warning alert-dismissible" role="alert">
            <div class="center-v">
                <fmt:message key="message.attachment.info.part1" /> <b>${AppProperty.APP_ATTACHMENT_COUNT}</b>. <fmt:message key="message.attachment.info.part2" /> <b>${AppProperty.APP_ATTACHMENT_SIZE} <fmt:message key="message.mb" /></b>
            </div>
        </div>
        <label class="btn btn-primary" for="file-selector">
            <input id="file-selector" type="file" multiple name="file" style="display:none">
            <fmt:message key="label.uploadAttachments" />
        </label>
        <ul class="attachments" id="attachments-list"></ul>
    </div>

    <div class="form-group">
        <button type="submit" class="btn btn-primary btn-block">
            <fmt:message key="label.create.button" />
        </button>
    </div>
</form>
<script>
    $(function () {
        dataForms.initSummernote('text', '<fmt:message key="label.question.placeholder" />');
        attacher.init('file-selector', 'attachments-list', ${AppProperty.APP_ATTACHMENT_COUNT}, ${AppProperty.APP_ATTACHMENT_SIZE});
    })
</script>
<jsp:include page="layout/footer.jsp" />