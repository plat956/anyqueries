<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${!empty message}">
    <div class="alert alert-${message.type.mode} alert-dismissible" role="alert">
        <div class="center-v">
                ${message.text}
        </div>
        <c:if test="${!empty message.notice}">
            <hr>
            <p class="mb-0 small opacity-70">
                    ${message.notice}
            </p>
        </c:if>
    </div>
</c:if>