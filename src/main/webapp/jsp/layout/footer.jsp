<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
                    </div>
                </div>
                <div class="bottom-copyright">
                    © ${currentYear} <a href="http://t.me/${initParam['author-telegram']}" target="_blank">${initParam['author-name']}</a>
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
    });

    $(function () {
        //init bootstrap forms validation
        (function() {
            'use strict';
            window.addEventListener('load', function() {
                var forms = document.getElementsByClassName('needs-validation');
                var validation = Array.prototype.filter.call(forms, function(form) {
                    form.addEventListener('submit', function(event) {
                        if (form.checkValidity() === false) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                });
            }, false);
        })();

        //init form buttons spinner
        forms.spinSubmitButton();

        //init popovers
        $('[data-toggle="popover"]').popover();

        //init selects
        $('select').selectpicker();
    });
</script>
</body>
</html>