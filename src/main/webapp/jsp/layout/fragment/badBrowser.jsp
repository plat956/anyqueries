<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="global-errors">
    <noscript>
        <div id="err-js-support" class="alert alert-danger" role="alert">
            ${fn:toUpperCase(initParam['website'])} <fmt:message key="message.noJavascript.warning" />
        </div>
    </noscript>
    <div id="err-cookie-support" class="alert alert-danger" role="alert" style="display: none">
        ${fn:toUpperCase(initParam['website'])} <fmt:message key="message.noCookie.warning" />
    </div>
</div>