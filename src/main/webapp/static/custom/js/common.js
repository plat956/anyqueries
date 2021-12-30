var dataForms = {
    spinSubmitButton: function () {
        $("form").on("submit", function() {
            if($(this)[0].checkValidity()) {
                var button = $("button[type=submit]", this);
                if(!button.hasClass('no-loader')) {
                    button.prop("disabled", true);
                    button.html('<span class="spinner-grow spinner-grow-sm"></span> ' + message.processing);
                }
            }
        });
    },
    uploadFileValidation: function (i) {
        const valid = [...i.files].every(file => {
            if (!i.accept) {
                return true;
            }
            return i.accept.replace(/\s/g, '').split(',').filter(accept => {
                return new RegExp(accept.replace('*', '.\*')).test(file.type);
            }).length > 0;
        });
        return valid;
    },
    uploadAvatar: function (input, form) {
        var result = dataForms.uploadFileValidation(input);
        if(result) {
            $('#' + form).submit();
        } else {
            toasts.show("error", message.wrong_file_format, message.allowed_file_formats + $(input).attr('accept'));
        }
    }
}

var registration = {
    sendLink: function (el) {
        if($(el).is(':checked')) {
            $('#email').prop('required', true);
            $('#telegram').prop('required', false);
        } else {
            $('#email').prop('required', false);
            $('#telegram').prop('required', true);
        }
    }
}

var pageEvents = {
    freeze: true,
    handler: function (e) {
        if (pageEvents.freeze) {
            e.stopPropagation();
            e.preventDefault();
        }
    },
    freezeClicks: function (s) {
        pageEvents.freeze = s;
        if (s) {
            $('body').addClass('loading');
        } else {
            $('body').removeClass('loading');
        }
    },
    init: function () {
        document.addEventListener("click", pageEvents.handler, true);
    },
    noBack: function () {
        window.history.forward();
    }
}

jQuery.fn.preventDoubleSubmission = function () {
    $(this).on('submit', function (e) {
        var $form = $(this);
        if($form[0].checkValidity()) {
            if ($form.data('submitted') === true) {
                e.preventDefault();
            } else {
                $form.data('submitted', true);
            }
        }
    });
    return this;
};

var toasts = {
    show: function (type, text, notice) {
        var options = {
            positionClass: "toast-top-right",
            timeOut: 5e3,
            closeButton: !0,
            debug: !1,
            newestOnTop: !0,
            progressBar: !0,
            preventDuplicates: !0,
            onclick: null,
            showDuration: "300",
            hideDuration: "1000",
            extendedTimeOut: "1000",
            showEasing: "swing",
            hideEasing: "linear",
            showMethod: "fadeIn",
            hideMethod: "fadeOut",
            tapToDismiss: !1
        };

        if(type == 'success') {
            toastr.success(notice, text, options);
        } else if(type == 'info') {
            toastr.info(notice, text, options);
        } else if(type == 'warning') {
            toastr.warning(notice, text, options);
        } else {
            toastr.error(notice, text, options);
        }
    }
}