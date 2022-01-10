var dataForms = {
    spinSubmitButton: function () {
        $("form").on("submit", function() {
            if($(this)[0].checkValidity()) {
                var button = $("button[type=submit]", this);
                if(!button.hasClass('no-loader')) {
                    button.prop("disabled", true);
                    button.data('old-text', button.text());
                    button.html('<span class="spinner-grow spinner-grow-sm"></span> ' + message.processing);
                }
                pageEvents.freezeClicks(true);
            }
        });
    },
    uploadFileCheckExtensions: function (i) {
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
    uploadFileCheckSize: function (i) {
        const valid = [...i.files].every(file => {
            var sizeInMB = (file.size / (1024*1024)).toFixed(2);
            return $(i).data("max-size") > sizeInMB;
        });
        return valid;
    },
    uploadAvatar: function (input, form) {
        var result = dataForms.uploadFileCheckExtensions(input);
        if(result) {
            result = dataForms.uploadFileCheckSize(input);
            if(result) {
                $('#' + form).submit();
            } else {
                toasts.show("error", message.error, message.wrong_file_size + $(input).data("max-size") + message.incorrect_files_size_part2);
            }
        } else {
            toasts.show("error", message.wrong_file_format, message.allowed_file_formats + $(input).attr('accept'));
        }
    },
    stripHtml: function(html) {
        let tmp = document.createElement("DIV");
        tmp.innerHTML = html;
        return tmp.textContent || tmp.innerText || "";
    },
    initSummernote: function (textarea, placeholder, lang) {
        var el = $('#' + textarea).summernote({
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['para', ['ul']],
                ['insert', ['link']]
            ],
            followingToolbar: false,
            minHeight: 250,
            placeholder: placeholder,
            lang: lang,
            callbacks: {
                onInit: function (){
                    var text = document.getElementById(textarea);
                    var id = textarea + '-counter';
                    $('#' + id).text($('#' + textarea).attr('maxlength') - dataForms.stripHtml(text.value).trim().length);
                    if($('#' + textarea).attr('required') && $.trim(text.value).length == 0) {
                        text.setCustomValidity("error");
                    } else {
                        text.setCustomValidity("");
                    }
                },
                onKeydown: function (e) {
                    var t = e.currentTarget.innerText;
                    if (t.trim().length >= $('#' + textarea).attr('maxlength')) {
                        if (e.keyCode != 8 && !(e.keyCode >=37 && e.keyCode <=40) && e.keyCode != 46 && !(e.keyCode == 88 && e.ctrlKey) && !(e.keyCode == 67 && e.ctrlKey) && !(e.keyCode == 65 && e.ctrlKey))
                            e.preventDefault();
                    }
                },
                onChange: function(contents) {
                    var id = textarea + '-counter';
                    $('#' + id).text($('#' + textarea).attr('maxlength') - dataForms.stripHtml(contents).trim().length);
                    var text = document.getElementById(textarea);
                    if($('#' + textarea).attr('required') && el.summernote('isEmpty')) {
                        text.setCustomValidity("error");
                    } else {
                        text.setCustomValidity("");
                    }
                },
                onPaste: function (e) {
                    var t = e.currentTarget.innerText;
                    var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                    e.preventDefault();
                    var maxPaste = bufferText.length;
                    if(t.length + bufferText.length > $('#' + textarea).attr('maxlength')){
                        maxPaste = $('#' + textarea).attr('maxlength') - t.length;
                    }
                    if(maxPaste > 0){
                        document.execCommand('insertText', false, bufferText.substring(0, maxPaste));
                    }
                    var id = textarea + '-counter';
                    $('#' + id).text($('#' + textarea).attr('maxlength') - t.length);
                }
            }
        });
    },
    bsSelectValidation: function () {
        if ($("form").hasClass('was-validated')) {
            $(".selectpicker").each(function (i, el) {
                if ($(el).is(":invalid")) {
                    $(el).closest(".form-group").find(".valid-feedback").removeClass("d-block");
                    $(el).closest(".form-group").find(".invalid-feedback").addClass("d-block");
                }
                else {
                    $(el).closest(".form-group").find(".invalid-feedback").removeClass("d-block");
                    $(el).closest(".form-group").find(".valid-feedback").addClass("d-block");
                }
            });
        }
    },
    reset: function(q) {
        pageEvents.freezeClicks(false);
        $('.support-content form').trigger('reset');
        var button = $(".support-content form button[type=submit]");
        button.prop("disabled", false);
        button.html(button.data('old-text'));
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

var attacher = {
    file: '',
    list: '',
    maxFiles: 0,
    fileSize: 0,
    updateList: function (files) {
        Array.prototype.forEach.call(files, (file) => {
            let li=document.createElement("li");
            li.innerHTML="<span><i class='fa fa-file file-attachment' aria-hidden='true'></i>"+file.name+"</span>";
            attacher.list.appendChild(li);
        });
    },
    init: function (input, list, maxFiles, fileSize) {
        attacher.maxFiles = maxFiles;
        attacher.fileSize = fileSize;
        attacher.file = document.getElementById(input);
        attacher.list = document.getElementById(list);

        attacher.file.addEventListener("change",(e)=>{
            attacher.list.innerHTML = "";

            if (attacher.file.files.length > attacher.maxFiles) {
                toasts.show("error", message.incorrect_files_count_part0, message.incorrect_files_count_part1 + attacher.maxFiles + message.incorrect_files_count_part2);
                attacher.file.value = null;
            } else {
                var allowSize = true;
                for(var i=0; i< attacher.file.files.length; i++){
                    var f = attacher.file.files[i];
                    var sizeInMB = (f.size / (1024*1024)).toFixed(2);
                    if(sizeInMB > attacher.fileSize) {
                        allowSize = false;
                        break;
                    }
                }

                if(allowSize) {
                    attacher.updateList(e.target.files);
                } else {
                    toasts.show("error", message.incorrect_files_size_part0, message.incorrect_files_size_part1 + attacher.fileSize + message.incorrect_files_size_part2);
                    attacher.file.value = null;
                }
            }
        });
    }
}

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
    noBack: function() {
        window.history.forward(1);
    }
}

var questions = {
    showProfile: function (context, id, ev) {
        ev.preventDefault();
        ev.stopPropagation();
        $.ajax({
            url: context + "/controller?command=profile_page",
            data: {id: id, ajax: true},
            dataType: 'json',
            success: function (res) {
                if(res == undefined || res == null || $.isEmptyObject(res)) {
                    toasts.show("error", message.incorrect_profile, message.incorrect_profile_text);
                } else {
                    $('.modal-title').text(res.fio);
                    if (res.avatar != undefined) {
                        $('.img-thumbnail').attr('src', context + '/controller?command=show_image&file=' + res.avatar);
                    } else {
                        $('.img-thumbnail').attr('src', context + '/static/custom/images/noavatar.png');
                    }
                    $('.user-role-span').text(res.role_name);
                    $('.user-role-span').removeClass(function (index, className) {
                        return (className.match(/(^|\s)badge-\S+/g) || []).join(' ');
                    });
                    $('.user-role-span').addClass('badge-' + res.role_color);
                    $('#fname').text(res.first_name);
                    $('#lname').text(res.last_name);
                    $('#mname').text(res.middle_name);
                    $('#email').text(res.email);
                    if (res.telegram != undefined) {
                        $('#telegram').html('<a href="' + res.telegram_lnk + '" target="_blank">@' + res.telegram + '</a>');
                    } else {
                        $('#telegram').empty();
                    }
                    if (res.email != undefined) {
                        $('#email').html('<a href="mailto:' + res.email + '" target="_blank">' + res.email + '</a>');
                    } else {
                        $('#email').empty();
                    }
                    $('#q_count').text(res.q_count);
                    $('#a_count').text(res.a_count);
                    $('#showProfileModal').modal('show');
                }
            }
        });
    },
    delete: function (ev, context, id) {
        ev.stopPropagation();
        bootbox.confirm({
            title: message.warn,
            message: message.delete_question,
            buttons: {
                confirm: {
                    label: message.confirm,
                    className: "btn-danger"
                },
                cancel: {
                    label: message.cancel,
                    className: "btn-secondary"
                }
            },
            callback: function (result) {
                if(result) {
                    $(document.body).append('<form action="' + context + '/controller?command=delete_question" method="post" style="display: none" id="deleteForm">' +
                        '<input type="hidden" name="id" value="' + id + '">' +
                        '</form>');
                    $('#deleteForm').submit();
                }
            }
        });
    }
}