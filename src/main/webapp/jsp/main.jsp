<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ft" uri="formtags" %>
<c:set var="page_title_label" value="label.main" scope="request" />
<jsp:include page="layout/header.jsp" />


        <button type="button" class="btn btn-outline-secondary active"><span class="badge badge-light">44</span> Открытые</button>
        <button type="button" class="btn btn-outline-secondary"><span class="badge badge-secondary">14</span> Закрытые</button>

        <div class="btn-group">
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Сортировка: <b>Новые <i class="fa fa-sort-alpha-asc" aria-hidden="true"></i></b>
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton" x-placement="bottom-start" style="position: absolute; transform: translate3d(0px, 38px, 0px); top: 0px; left: 0px; will-change: transform;">
                    <a class="dropdown-item" href="#">Новые <i class="fa fa-sort-alpha-desc" aria-hidden="true"></i></a>
                    <a class="dropdown-item" href="#">Самые обсуждаемые <i class="fa fa-sort-alpha-asc" aria-hidden="true"></i></a>
                </div>
            </div>
            <div class="form-check" style="padding:5px;margin-left:30px">
                <input type="checkbox" class="form-check-input" id="exampleCheck1">
                <label class="form-check-label" for="exampleCheck1">Имеют решение</label>
            </div>
        </div>
        <!-- BEGIN NEW TICKET -->
        <button type="button" class="btn btn-success float-right" data-toggle="modal" data-target="#newIssue">Задать вопрос</button>

        <!-- END NEW TICKET -->
        <div class="padding"></div>
        <div class="row">
            <!-- BEGIN TICKET CONTENT -->
            <div class="col-lg-12" style="
    padding: 0;
    border-radius: 0px;
">
                <ul class="list-group fa-padding" style="
    margin-left: -5px;
    margin-right: -5px;
">
                    <li class="list-group-item" data-toggle="modal" data-target="#issue" style="
    border-radius: 0;
    border-left: 0;
    border-right: 0;
">
                        <div class="media"><i class="fa fa-quote-right" aria-hidden="true"></i>
                            <div class="media-body">	<strong>Сколько будет 2*2?</strong>
                                <span class="number float-right"># 13698</span>
                                <p class="info">Автор: <a href="#">jwilliams</a> 5 минут назад <i class="fa fa-comments"></i>
                                    2 ответа
                                </p>
                            </div>
                        </div>
                    </li>
                    <li class="list-group-item" data-toggle="modal" data-target="#issue" style="
    border-radius: 0;
    border-left: 0;
    border-right: 0;
">
                        <div class="media">	<i class="fa fa-quote-right" aria-hidden="true"></i>
                            <div class="media-body">	<strong>Какой сейчас год?</strong>
                                <span class="number float-right"># 13697</span>
                                <p class="info">Автор: <a href="#">lgardner</a> 2 дня назад <i class="fa fa-comments"></i>
                                    7 ответов
                                </p>
                            </div>
                        </div>
                    </li>
                </ul>

            </div>
            <!-- END TICKET CONTENT -->
        </div>
<nav >
    <ul class="pagination justify-content-center" style="padding-bottom: 0px;margin-bottom: 0px;margin-top: 15px;">
        <li class="page-item">
            <a class="page-link" href="#" aria-label="Previous">
                <span aria-hidden="true">«</span>
                <span class="sr-only">Previous</span>
            </a>
        </li>
        <li class="page-item active"><a class="page-link" href="#">1</a></li>
        <li class="page-item"><a class="page-link" href="#">2</a></li>
        <li class="page-item"><a class="page-link" href="#">3</a></li>
        <li class="page-item">
            <a class="page-link" href="#" aria-label="Next">
                <span aria-hidden="true">»</span>
                <span class="sr-only">Next</span>
            </a>
        </li>
    </ul>
</nav>
<jsp:include page="layout/footer.jsp" />