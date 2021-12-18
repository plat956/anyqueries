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