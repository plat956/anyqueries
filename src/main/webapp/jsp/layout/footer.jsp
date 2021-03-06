<%@ page contentType="text/html;charset=UTF-8" language="java" import="by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
</div>
</div>
<div class="bottom-copyright">
    © ${currentYear} <a href="${AppProperty.APP_TELEGRAM_LINK_HOST}${AppProperty.APP_DEVELOPER_TELEGRAM}" target="_blank">
        ${AppProperty.APP_DEVELOPER_FIRST_NAME} ${AppProperty.APP_DEVELOPER_LAST_NAME}
    </a>
</div>
</div>
</div>
</section>
</div>
<script type="text/javascript">
    pageEvents.init();

    // Show the progress bar
    NProgress.start();

    // Increase randomly
    var interval = setInterval(function() { NProgress.inc(); }, 1000);

    // Trigger finish when page fully loaded
    $(window).on('pageshow', function(){
        dataForms.reset();
        clearInterval(interval);
        NProgress.done();
        pageEvents.freezeClicks(false);
        setTimeout(function () {
            $('#container').css('visibility', 'visible');
        }, 100)
    });

    // Trigger bar when exiting the page
    $(window).on('beforeunload', function(e){
        NProgress.start();
        pageEvents.freezeClicks(true);
    });

    $(function () {
        //tooltips init
        $('[data-toggle="tooltip"]').tooltip();

        //prevent f5 submitting form data
        if (window.history.replaceState) {
            window.history.replaceState(null, null, window.location.href);
        }

        <c:if test="${!bad_browser_command}">
            //check if cookies are enabled
            if (!navigator.cookieEnabled) {
                location.href = '${pageContext.request.contextPath}/controller?command=bad_browser_page';
            }
        </c:if>

        //init bootstrap forms validation
        (function() {
            'use strict';
            window.addEventListener('load', function() {
                var allForms = document.getElementsByClassName('needs-validation');
                var validation = Array.prototype.filter.call(allForms, function(form) {
                    form.addEventListener('submit', function(event) {
                        if (form.checkValidity() === false) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        $('.invalid-feedback-backend').remove();
                        form.classList.add('was-validated');
                        dataForms.bsSelectValidation();
                        dataForms.bsSummernoteValidation();
                    }, false);
                });
            }, false);
        })();

        $('form').change(dataForms.bsSelectValidation);
        $('form').change(dataForms.bsSummernoteValidation);

        //init navbar live search
        $('#search-input').typeahead({
            afterSelect: function (item) {
                $('#searchInput').val(item);
                $('#searchForm').submit();
            },
            source:  function (query, process) {
                return $.get('${pageContext.request.contextPath}/controller?command=live_search',
                    {
                        query_string: query,
                        ajax: true,
                        <c:if test="${!empty principal && param['mode'] == 'my'}">current: true,</c:if>
                        <c:if test="${!empty principal && principal.role == 'ADMIN' && param['command'] == 'users_page'}">users: true,</c:if>
                        <c:if test="${param['command'] == 'categories_page'}">categories: true,</c:if>
                        <c:if test="${!empty param['category'] && param['category'].matches('[0-9]+') && !empty category_name}">category: '${fn:escapeXml(param['category'])}',</c:if>
                    },
                    function (data) {
                        $('#noResults').remove();
                        if(data.length == 0) {
                            $('#search-input').after('' +
                                '<ul class="typeahead dropdown-menu" id="noResults" role="listbox"">' +
                                '<li class="active">' + message.no_results + '</li>' +
                                '</ul>')
                        }
                        return process(data);
                    });
            }
        }).on('input', function () {
            var value = $(this).val();
            if(value.length == 0) {
                $('#noResults').remove();
                $('#clearSearch').css('visibility', 'hidden');
            } else {
                $('#clearSearch').css('visibility', 'visible');
            }
        });

        //init form buttons spinner
        dataForms.spinSubmitButton();

        //init popovers
        $('[data-toggle="popover"]').popover();

        //init selects
        $('select').each(function (i, el) {
            $(el).selectpicker();
            if($(el).hasClass('error-invalid')) {
                $(el).selectpicker('setStyle', 'error-invalid');
            }
        });
    });
</script>
</body>
</html>