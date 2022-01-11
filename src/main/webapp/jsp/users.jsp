<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="page_title_label" value="label.users" scope="request"/>
<jsp:include page="layout/header.jsp"/>
<style>
    .page-title-hr {
        display: none !important;
    }
</style>
<div class="row" style="padding-top:10px">
    <div class="col-lg-12" style="
    padding: 0;
    border-radius: 0px;">
        <ul class="list-group fa-padding questions-group">
            <c:forEach var="q" items="${users}">
                <li class="list-group-item" style="border-radius: 0;border-left: 0;border-right: 0;" onclick="questions.showProfile('${pageContext.request.contextPath}', ${q.id}, event); return false;">
                    <div class="media" style="font-size: 15px;">
                        <div class="media-body"><span class="badge badge-${q.role.color} user-role-span"><fmt:message key="label.role.${fn:toLowerCase(q.role)}" /></span>&nbsp;&nbsp;<strong>${q.login}</strong> <span>(${q.fio})</span>
                            <span class="number float-right">
                                    <a onclick="event.stopPropagation();location.href = '${pageContext.request.contextPath}/controller?command=edit_user_page&id=${q.id}'" data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.edit" />">
                                        <i class="fa fa-edit" aria-hidden="true" style="color: #007bff"></i>
                                    </a>
                                    <a onclick="users.delete(event, '${pageContext.request.contextPath}', ${q.id})" data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.delete" />">
                                        <i class="fa fa-trash" aria-hidden="true" style="color: red"></i>
                                    </a>
                                </span>
                        </div>
                    </div>
                </li>
            </c:forEach>
            <c:if test="${empty users}">
                <div class="alert alert-secondary" role="alert" style="margin: 0px 15px 15px 15px;">
                    <fmt:message key="message.no.results" />
                </div>
            </c:if>
        </ul>
    </div>
</div>
<jsp:include page="fragment/pagination.jsp" />
<jsp:include page="fragment/showProfileModal.jsp" />
<jsp:include page="layout/footer.jsp"/>