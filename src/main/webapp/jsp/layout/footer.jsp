<%@ page contentType="text/html;charset=UTF-8" language="java" import="by.latushko.anyqueries.util.AppProperty" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    // Show the progress bar
    NProgress.start();

    // Increase randomly
    var interval = setInterval(function() { NProgress.inc(); }, 1000);

    // Trigger finish when page fully loaded
    $(window).load(function () {
        clearInterval(interval);
        NProgress.done();
    });

    // Trigger bar when exiting the page
    $(window).on('beforeunload', function(){
        NProgress.start();
        pageEvents.freezeClicks(true);
    });

    //prevent forms double submitting
    $('form').preventDoubleSubmission();

    $(document).on('submit', 'form', function(e) {
        if($(this)[0].checkValidity()) {
            pageEvents.freezeClicks(true);
        }
    });

    $(function () {
        //check if cookies are enabled
        if (!navigator.cookieEnabled) {
            $('#err-cookie-support').show();
        }

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
                    }, false);
                });
            }, false);
        })();

        //init navbar live search
        $('input.search-input').typeahead({
            source:  function (query, process) {
                return $.get('/controller?command=live_search', {query_string: query, ajax: true}, function (data) {
                    return process(data);
                });
            }
        });

        //init form buttons spinner
        forms.spinSubmitButton();

        //init popovers
        $('[data-toggle="popover"]').popover();

        //init selects
        $('select').selectpicker();

        setTimeout(function() {
            pageEvents.freezeClicks(false);
        }, 1);
    });
</script>
</body>
</html>