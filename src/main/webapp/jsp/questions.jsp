<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ft" uri="formtags" %>
<c:set var="page_title_label" value="label.allQuestions" scope="request"/>
<jsp:include page="layout/header.jsp"/>
<div class="btn-group">
    <div class="dropdown">
        <button class="btn btn-outline-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown"
                aria-haspopup="true" aria-expanded="false">
            <fmt:message key="label.sort" />: <b><fmt:message key="label.questions.new" /> <i class="fa fa-sort-alpha-asc" aria-hidden="true"></i></b>
        </button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton" x-placement="bottom-start"
             style="position: absolute; transform: translate3d(0px, 38px, 0px); top: 0px; left: 0px; will-change: transform;">
            <a class="dropdown-item" href="#"><fmt:message key="label.questions.new" /> <i class="fa fa-sort-alpha-desc" aria-hidden="true"></i></a>
            <a class="dropdown-item" href="#"><fmt:message key="label.questions.discussed" /> <i class="fa fa-sort-alpha-asc" aria-hidden="true"></i></a>
        </div>
    </div>
    <div class="has-solution">
        <label class="switch">
            <input type="checkbox" class="form-check-input" data-toggle="switchbutton" name="remember_me" id="rememberMe"${!empty param['resolved'] ? ' checked' : ''}>
            <span class="slider round"></span>
        </label>
        <label class="form-check-label" for="rememberMe"><fmt:message key="label.questions.solution" /></label>
    </div>
</div>
<a class="btn btn-success float-right create-question" data-toggle="modal"
   href="${pageContext.request.contextPath}/controller?command=create_question_page"><fmt:message key="label.questions.create" /></a>

<div class="padding"></div>
<div class="row">
    <div class="col-lg-12" style="
    padding: 0;
    border-radius: 0px;
">
        <ul class="list-group fa-padding questions-group">
            <c:forEach var="q" items="${questions}">
                <li class="list-group-item" style="border-radius: 0;border-left: 0;border-right: 0;" onclick="location.href = '${pageContext.request.contextPath}/controller?command=question_page&id=${q.id}'">
                    <div class="media"><i class="fa fa-question-circle" aria-hidden="true"></i>
                        <div class="media-body"><strong>${q.title}</strong>
                            <span class="number float-right"># ${q.id}</span>
                            <p class="info"><fmt:message key="label.author" />: <a class="author-lnk" onclick="alert();">${q.author.fio}</a> <ft:time-duration date="${q.creationDate}"/> <i class="fa fa-comments"></i>
                                <ft:plural-formatter count="${q.answersCount}" key="label.answer"/>
                            </p>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<jsp:include page="fragment/pagination.jsp" />
<jsp:include page="layout/footer.jsp"/>