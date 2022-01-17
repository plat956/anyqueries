<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="at" uri="apptags" %>
<c:choose>
    <c:when test="${!empty category}">
        <c:set var="page_title" value="${category_name}" scope="request"/>
    </c:when>
    <c:when test="${param['mode'] == 'my' && !empty principal}">
        <c:set var="page_title_label" value="label.myQuestions" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:set var="page_title_label" value="label.allQuestions" scope="request"/>
    </c:otherwise>
</c:choose>
<c:if test="${!empty param['query']}">
    <c:set var="page_title_postfix" value=": «${fn:substring(param['query'], 0, 40)}»" scope="request" />
</c:if>
<jsp:include page="layout/header.jsp"/>
<div class="btn-group">
    <div class="dropdown">
        <button class="btn btn-outline-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown"
                aria-haspopup="true" aria-expanded="false">
            <fmt:message key="label.sort" />:<b>
            <c:choose>
                <c:when test="${param['sort'] == 'discussed'}">
                    <fmt:message key="label.questions.discussed" />
                </c:when>
                <c:otherwise>
                    <fmt:message key="label.questions.new" />
                </c:otherwise>
            </c:choose>
        </b>
        </button>

        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton" x-placement="bottom-start"
             style="position: absolute; transform: translate3d(0px, 38px, 0px); top: 0px; left: 0px; will-change: transform;">
            <a class="dropdown-item" href="${pageContext.request.contextPath}<at:query-parameter-changer key="sort" value="new"/>"><fmt:message key="label.questions.new" /> <i class="fa fa-sort-alpha-desc" aria-hidden="true"></i></a>
            <a class="dropdown-item" href="${pageContext.request.contextPath}<at:query-parameter-changer key="sort" value="discussed"/>"><fmt:message key="label.questions.discussed" /> <i class="fa fa-sort-alpha-asc" aria-hidden="true"></i></a>
        </div>
    </div>
    <div class="has-solution">
        <label class="switch">
            <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="resolved" id="resolved"
                   onchange="changeResolved(this)"
                   id="rememberMe"${param['resolved'] ? ' checked' : ''}>
            <span class="slider round"></span>
        </label>
        <label class="form-check-label" for="resolved"><fmt:message key="label.questions.solution" /></label>
    </div>
</div>
<c:if test="${!empty principal}">
<a class="btn btn-success float-right create-question" data-toggle="modal"
   href="${pageContext.request.contextPath}/controller?command=create_question_page<c:if test="${!empty category}">&category=${category}</c:if>"><fmt:message key="label.questions.create" /></a>
</c:if>
<div class="padding"></div>
<div class="row">
    <div class="col-lg-12" style="
    padding: 0;
    border-radius: 0px;
">
        <ul class="list-group fa-padding questions-group">
            <c:forEach var="q" items="${questions}">
                <li class="list-group-item" style="border-radius: 0;border-left: 0;border-right: 0;" onclick="location.href = '${pageContext.request.contextPath}/controller?command=question_page&id=${q.id}'">
                    <div class="media">
                        <i class="${q.solved ? 'fas fa-check' : 'far fa-question'}-circle" aria-hidden="true" style="position: absolute;top: 20px;color: ${q.solved ? '#28a745' : ''}"></i>
                        <c:if test="${q.closed}">
                            <i class="fas fa-lock" style="position: absolute;bottom: 20px;color:#eb870a;"></i>
                        </c:if>
                        <div class="media-body" style="margin-left: 30px;"><strong class="break-words">${q.title}</strong>
                            <c:if test="${!empty principal}">
                                <span class="number float-right" style="margin-top: 12px;">
                                    <c:if test="${!q.closed && q.author.id == principal.id}">
                                        <a onclick="event.stopPropagation();location.href = '${pageContext.request.contextPath}/controller?command=edit_question_page&id=${q.id}'" data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.edit" />">
                                            <i class="fa fa-edit" aria-hidden="true" style="color: #007bff"></i>
                                        </a>
                                    </c:if>
                                    <c:if test="${q.author.id == principal.id || principal.role == 'ADMIN' || principal.role == 'MODERATOR'}">
                                        <a onclick="questions.delete(event, '${pageContext.request.contextPath}', ${q.id}, false)" data-toggle="tooltip" data-placement="top" title="<fmt:message key="label.delete" />">
                                            <i class="fa fa-trash" aria-hidden="true" style="color: red"></i>
                                        </a>
                                    </c:if>
                                </span>
                            </c:if>
                            <p class="info"><fmt:message key="label.author" />: <a class="author-lnk" onclick="questions.showProfile('${pageContext.request.contextPath}', ${q.author.id}, event); return false;">${q.author.fio}</a> <at:time-duration date="${q.creationDate}"/> <i class="fa fa-comments"></i>
                                <at:plural-formatter count="${q.answersCount}" key="label.answer"/>
                            </p>
                        </div>
                    </div>
                </li>
            </c:forEach>
            <c:if test="${empty questions}">
                <div class="alert alert-secondary" role="alert" style="margin: 0px 15px 15px 15px;">
                    <fmt:message key="message.no.results" />
                </div>
            </c:if>
        </ul>
    </div>
</div>
<jsp:include page="fragment/pagination.jsp" />
<c:set var="current_without_page" scope="request">
    <at:query-parameter-changer key="page" value=""/>
</c:set>
<script>
    function changeResolved(el) {
        if($(el).is(":checked")) {
            location.href = '${pageContext.request.contextPath}<at:query-parameter-changer url="${current_without_page}" key="resolved" value="true"/>';
        } else {
            location.href = '${pageContext.request.contextPath}<at:query-parameter-changer url="${current_without_page}" key="resolved" value="false"/>';
        }
    }
</script>
<jsp:include page="fragment/showProfileModal.jsp" />
<jsp:include page="layout/footer.jsp"/>