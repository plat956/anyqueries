<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${!empty message}">
    <c:choose>
        <c:when test="${message.type == 'ALERT'}">
            <div class="alert alert-${message.level.mode} alert-dismissible" role="alert">
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
        </c:when>
        <c:when test="${message.type == 'POPUP'}">
            <script type="text/javascript">
                $(function () {
                    swal({
                        title: "${message.text}",
                        text: "${message.notice}",
                        type: "${message.level.mode}",
                        confirmButtonClass: "btn-${message.level.mode}",
                        closeOnConfirm: false
                    });
                });
            </script>
        </c:when>
        <c:when test="${message.type == 'TOAST'}">
            <script type="text/javascript">
                $(function () {
                    toasts.show("${message.level.mode}", "${message.text}", "${message.notice}");
                });
            </script>
        </c:when>
    </c:choose>
</c:if>