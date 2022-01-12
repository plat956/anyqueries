<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="by.latushko.anyqueries.controller.command.identity.RequestParameter,
         by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title_label" value="label.createQuestion" scope="request" />
<jsp:include page="layout/header.jsp" />
<form id="create_form" class="needs-validation" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/controller?command=create_question" novalidate autocomplete="off">
    <div class="form-group">
        <select class="selectpicker" data-width="100%" name="category" required>
            <option value="" selected disabled><fmt:message key="label.category.placeholder" /></option>
            <c:set var="selected_category" value="${!empty validationResult ? validationResult.getValue(RequestParameter.CATEGORY) : category}" />
            <c:forEach var="c" items="${categories}">
                <option value="${c.id}"${selected_category == c.id  ? ' selected' : ''}>${c.name}</option>
            </c:forEach>
        </select>
        <c:if test="${!empty validationResult.getMessage(RequestParameter.CATEGORY)}">
            <div class="invalid-feedback-backend">
                <fmt:message key="${validationResult.getMessage(RequestParameter.CATEGORY)}" />
            </div>
        </c:if>
        <div class="invalid-feedback">
            <fmt:message key="label.wrong-input" />
        </div>
    </div>
    <div class="form-group">
        <textarea name="title" class="form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.TITLE)}" />"
               placeholder="<fmt:message key="label.questionTitle.placeholder" />" type="text" required maxlength="200" style="min-height: 70px;max-height: 150px;resize: vertical"
               data-toggle="popover" data-trigger="focus" data-placement="right"
               data-content="<fmt:message key="label.questionTitle.notice" />"
        >${validationResult.getValue(RequestParameter.TITLE)}</textarea>
        <c:if test="${!empty validationResult.getMessage(RequestParameter.TITLE)}">
            <div class="invalid-feedback-backend">
                <fmt:message key="${validationResult.getMessage(RequestParameter.TITLE)}" />
            </div>
        </c:if>
        <div class="invalid-feedback">
            <fmt:message key="label.wrong-input" />
        </div>
    </div>
    <div class="form-group">
        <textarea id="text" name="text" class="summernote form-control<at:field-class-detector field="${validationResult.getField(RequestParameter.TEXT)}" />"
                  required maxlength="${AppProperty.APP_QUESTION_MAXLENGTH}">
            ${validationResult.getValue(RequestParameter.TEXT)}
        </textarea>
        <c:if test="${!empty validationResult.getMessage(RequestParameter.TEXT)}">
            <div class="invalid-feedback-backend">
                <fmt:message key="${validationResult.getMessage(RequestParameter.TEXT)}" />
            </div>
        </c:if>
        <div class="invalid-feedback">
            <fmt:message key="label.wrong-input" />
        </div>
        <fmt:message key="label.textarea.remaining" /> <span id="text-counter">${AppProperty.APP_QUESTION_MAXLENGTH}</span>
    </div>

    <div class="form-group">
        <label class="btn btn-primary" for="file-selector" data-toggle="popover" data-trigger="hover" data-placement="bottom"
               data-content="<fmt:message key="message.attachment.info.part1" /> ${AppProperty.APP_ATTACHMENT_COUNT}. <fmt:message key="message.attachment.info.part2" /> ${AppProperty.APP_ATTACHMENT_SIZE} <fmt:message key="message.mb" />">
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
        dataForms.initSummernote('text', '<fmt:message key="label.question.placeholder" />', '${!empty current_lang ? current_lang : 'ru'}', 250);
        attacher.init('file-selector', 'attachments-list', ${AppProperty.APP_ATTACHMENT_COUNT}, ${AppProperty.APP_ATTACHMENT_SIZE});
    })
</script>
<jsp:include page="layout/footer.jsp" />