<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="page_title_label" value="label.users" scope="request"/>
<c:if test="${!empty param['query']}">
    <c:set var="page_title_postfix" value=": «${fn:substring(param['query'], 0, 40)}»" scope="request" />
</c:if>
<jsp:include page="layout/header.jsp"/>
<c:if test="${!empty users}">
<style>
    .page-title-hr {
        display: none !important;
    }
</style>
</c:if>
<div class="row" style="padding-top:10px">
<c:if test="${!empty users}">
    <table class="table table-hover" style="margin-bottom: 5px">
        <thead>
        <tr>
            <th scope="col"><fmt:message key="label.login.placeholder" /></th>
            <th scope="col"><fmt:message key="label.fio" /></th>
            <th scope="col"><fmt:message key="label.status" /></th>
            <th scope="col"><fmt:message key="label.role" /></th>
            <th scope="col"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="q" items="${users}">
            <tr style="cursor: pointer">
                <td onclick="questions.showProfile('${pageContext.request.contextPath}', ${q.id}, event); return false;"><c:out value="${q.login}" /></td>
                <td onclick="questions.showProfile('${pageContext.request.contextPath}', ${q.id}, event); return false;"><c:out value="${q.fio}" /></td>
                <td onclick="questions.showProfile('${pageContext.request.contextPath}', ${q.id}, event); return false;"><span class="badge badge-${q.status == 'ACTIVE' ? 'success' : (q.status == 'INACTIVE' ? 'secondary' : 'danger')} user-role-span"><fmt:message key="label.status.${fn:toLowerCase(q.status)}" /></span></td>
                <td onclick="questions.showProfile('${pageContext.request.contextPath}', ${q.id}, event); return false;"><span class="badge badge-${q.role.color} user-role-span"><fmt:message key="label.role.${fn:toLowerCase(q.role)}" /></span></td>
                <td align="right">
                    <a onclick="event.stopPropagation();location.href = '${pageContext.request.contextPath}/controller?command=edit_user_page&id=${q.id}'" style="cursor:pointer" data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.edit" />">
                        <i class="fa fa-edit" aria-hidden="true" style="color: #007bff"></i>
                    </a>&nbsp;
                    <c:if test="${q.id != 1}">
                        <a onclick="users.delete(event, '${pageContext.request.contextPath}', ${q.id})" data-toggle="tooltip" data-placement="top" style="cursor:pointer" title="<fmt:message key="label.delete" />">
                            <i class="fa fa-trash" aria-hidden="true" style="color: red"></i>
                        </a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty users}">
    <div class="alert alert-warning" role="alert" style="margin: -10px 15px 15px 15px;width:100%">
        <fmt:message key="message.no.results" />
    </div>
</c:if>
</div>
<jsp:include page="fragment/pagination.jsp" />
<jsp:include page="fragment/showProfileModal.jsp" />
<jsp:include page="layout/footer.jsp"/>