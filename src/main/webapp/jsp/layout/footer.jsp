<%@ page contentType="text/html;charset=UTF-8" language="java" import="by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    //disable back button
    pageEvents.noBack();

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
        $('#container').show();
    });

    // Trigger bar when exiting the page
    $(window).on('beforeunload', function(e){
        NProgress.start();
        pageEvents.freezeClicks(true);
    });

    $(function () {
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
                    }, false);
                });
            }, false);
        })();

        $('form').change(dataForms.bsSelectValidation);

        //init navbar live search
        $('#search-input').typeahead({
            source:  function (query, process) {
                return $.get('/controller?command=live_search',
                    {
                        query_string: query,
                        ajax: true,
                        <c:if test="${param['mode'] == 'my'}">current: true,</c:if>
                        <c:if test="${!empty param['category']}">category: '${param['category']}',</c:if>
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
            }
        });

        //init form buttons spinner
        dataForms.spinSubmitButton();

        //init popovers
        $('[data-toggle="popover"]').popover();

        //init selects
        $('select').selectpicker();
    });
</script>
</body>
</html>