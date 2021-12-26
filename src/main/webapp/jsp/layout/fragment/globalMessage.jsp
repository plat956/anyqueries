<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="global-messages">
    <noscript>
        <div id="err-js-support" class="alert alert-danger" role="alert">
            ${fn:toUpperCase(initParam['website'])} <fmt:message key="message.noJavascript.warning" />
        </div>
    </noscript>
    <div id="err-cookie-support" class="alert alert-danger" role="alert" style="display: none">
        ${fn:toUpperCase(initParam['website'])} <fmt:message key="message.noCookie.warning" />
    </div>
    <c:if test="${!empty inactivePrincipal}">
        <div id="warning-inactive" class="alert alert-warning" role="alert">
            <fmt:message key="message.activation.repeat.alert" /><a href="/controller?command=repeat_activation_page"><fmt:message key="message.activation.repeat.link" /></a>
        </div>
    </c:if>
</div>