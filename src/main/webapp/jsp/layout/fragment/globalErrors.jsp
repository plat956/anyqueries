<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="global-errors">
    <noscript>
        <div id="err-js-support" class="alert alert-danger" role="alert">
            ${fn:toUpperCase(initParam['website'])} requires javascript support to work properly! Please enable it in browser settings.
        </div>
    </noscript>
    <div id="err-cookie-support" class="alert alert-danger" role="alert" style="display: none">
        ${fn:toUpperCase(initParam['website'])} requires cookies support to work properly! Please enable it in browser settings.
    </div>
</div>