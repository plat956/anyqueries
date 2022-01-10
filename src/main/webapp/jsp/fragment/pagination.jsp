<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="at" uri="apptags" %>
<c:choose>
    <c:when test="${totalPages > 1}">
        <nav>
            <ul class="pagination justify-content-center">
                <c:set var="page" value="${!empty param['page'] ? param['page'] : 1}" />
                <c:if test="${page > 1}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}<at:query-parameter-changer key="page" value="${page - 1}"/>">
                            <span aria-hidden="true">«</span>
                        </a>
                    </li>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" varStatus="i">
                    <c:choose>
                        <c:when test="${i.index == page}">
                            <li class="page-item active">
                                <a class="page-link" href="${pageContext.request.contextPath}<at:query-parameter-changer key="page" value="${i.index}"/>">
                                        ${i.index}
                                </a>
                            </li>
                        </c:when>
                        <c:when test="${i.index > page + 2 && i.index != totalPages}">
                            <c:if test="${empty leftSkip}">
                                <c:set var="leftSkip" value="true" />
                                <li class="page-item">
                                    <a class="page-link">
                                        ...
                                    </a>
                                </li>
                            </c:if>
                        </c:when>
                        <c:when test="${i.index < page - 2 && i.index != 1}">
                            <c:if test="${empty rightSkip}">
                                <c:set var="rightSkip" value="true" />
                                <li class="page-item">
                                    <a class="page-link">
                                        ...
                                    </a>
                                </li>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item">
                                <a class="page-link" href="${pageContext.request.contextPath}<at:query-parameter-changer key="page" value="${i.index}"/>">
                                        ${i.index}
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${page < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}<at:query-parameter-changer key="page" value="${page + 1}"/>">
                            <span aria-hidden="true">»</span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </c:when>
    <c:otherwise>
        <div style="margin-top: -15px;"></div>
    </c:otherwise>
</c:choose>
