<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="page_title" value="Main page" scope="request" />
<jsp:include page="layout/header.jsp" />
Hello, ${principal.firstName}
<jsp:include page="layout/footer.jsp" />