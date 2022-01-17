<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="at" uri="apptags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="page_title_label" value="label.categories" scope="request"/>
<c:if test="${!empty param['query']}">
    <c:set var="page_title_postfix" value=": «${fn:substring(param['query'], 0, 40)}»" scope="request" />
</c:if>
<jsp:include page="layout/header.jsp"/>
<c:choose>
    <c:when test="${!empty principal && principal.role == 'ADMIN'}">
        <div class="btn-group" style="padding-bottom: 5px">
            <a class="btn btn-success float-right" data-toggle="modal"
               href="${pageContext.request.contextPath}/controller?command=create_category_page"><fmt:message key="label.categories.create" /></a>
        </div>
    </c:when>
    <c:otherwise>
        <style>
            .page-title-hr {
                display: none !important;
            }
        </style>
    </c:otherwise>
</c:choose>
<div class="row" style="padding-top:10px">
    <div class="col-lg-12" style="
    padding: 0;
    border-radius: 0px;">
        <ul class="list-group fa-padding questions-group">
            <c:forEach var="q" items="${categories}">
                <li class="list-group-item" style="border-radius: 0;border-left: 0;border-right: 0;" onclick="location.href = '${pageContext.request.contextPath}/controller?command=questions_page&category=${q.id}'">
                    <div class="media" style="font-size: 15px;"><i class="fa fa-folder-open" aria-hidden="true" style="color: ${q.color}"></i>
                        <div class="media-body"><strong>${q.name}</strong>
                            <c:if test="${!empty principal && principal.role == 'ADMIN'}">
                                <span class="number float-right">
                                        <a onclick="event.stopPropagation();location.href = '${pageContext.request.contextPath}/controller?command=edit_category_page&id=${q.id}'" data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.edit" />">
                                            <i class="fa fa-edit" aria-hidden="true" style="color: #007bff"></i>
                                        </a>
                                        <a onclick="categories.delete(event, '${pageContext.request.contextPath}', ${q.id})" data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.delete" />">
                                            <i class="fa fa-trash" aria-hidden="true" style="color: red"></i>
                                        </a>
                                </span>
                            </c:if>
                        </div>
                    </div>
                </li>
            </c:forEach>
            <c:if test="${empty categories}">
                <div class="alert alert-secondary" role="alert" style="margin: 0px 15px 15px 15px;">
                    <fmt:message key="message.no.results" />
                </div>
            </c:if>
        </ul>
    </div>
</div>
<jsp:include page="fragment/pagination.jsp" />
<jsp:include page="layout/footer.jsp"/>