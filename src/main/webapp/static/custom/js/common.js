var forms = {
    spinSubmitButton: function () {
        $("form").on("submit", function() {
            if($(this)[0].checkValidity()) {
                var button = $("button[type=submit]", this);
                button.prop("disabled", true);
                button.html('<span class="spinner-grow spinner-grow-sm"></span> Выполняется...');
            }
        });
    }
}

var registration = {
    chooseMode: function (value) {
        $('.notice-confirmation').hide();
        $('#' + value + '-confirmation').fadeIn();
        $('.input-confirmation').prop('required',false);
        $('#' + value).prop('required',true);
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