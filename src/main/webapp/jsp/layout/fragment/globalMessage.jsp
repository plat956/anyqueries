<%@ page contentType="text/html;charset=UTF-8" language="java" import="by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="global-messages">
    <c:if test="${!empty inactivePrincipal}">
        <div id="warning-inactive" class="alert alert-warning" role="alert">
            <fmt:message key="message.activation.repeat.alert" /><a href="/controller?command=repeat_activation_page"><fmt:message key="message.activation.repeat.link" /></a>
        </div>
    </c:if>
</div>