<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="at" uri="apptags" %>
<div class="modal fade" id="showProfileModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-3">
                        <div class="text-center">
                            <img src="" class="avatar img-circle img-thumbnail" alt="avatar">
                            <div class="panel panel-default text-center profile-role">
                                <div class="panel-body">
                                    <span class="badge user-role-span"></span>
                                </div>
                            </div>
                        </div>
                        <ul class="list-group" style="padding: 15px 0px 15px 0px;">
                            <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.questions" /></strong></span> <span id="q_count"></span></li>
                            <li class="list-group-item text-left"><span class="pull-left"><strong><fmt:message key="label.answers" /></strong></span> <span id="a_count"></span></li>
                        </ul>
                    </div>
                    <div class="col-sm-9" id="profile-nav">
                        <table class="table table-hover" style="cursor: default;">
                            <tbody>
                                <tr>
                                    <td style="border-top: none;"><i class="fa fa-user"></i> <b><fmt:message key="label.firstName.placeholder" /></b></td>
                                    <td style="border-top: none;" id="fname"></td>
                                </tr>
                                <tr>
                                    <td><i class="fa fa-user"></i> <b><fmt:message key="label.lastName.placeholder" /></b></td>
                                    <td id="lname"></td>
                                </tr>
                                <tr>
                                    <td><i class="fa fa-user"></i> <b><fmt:message key="label.middleName.placeholder" /></b></td>
                                    <td id="mname"></td>
                                </tr>
                                <tr>
                                    <td><i class="fa fa-envelope"></i> <b><fmt:message key="label.email.placeholder" /></b></td>
                                    <td id="email"></td>
                                </tr>
                                <tr>
                                    <td><i class="fab fa-telegram"></i> <b><fmt:message key="label.telegram.placeholder" /></b></td>
                                    <td id="telegram"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message key="label.close" /></button>
            </div>
        </div>
    </div>
</div>