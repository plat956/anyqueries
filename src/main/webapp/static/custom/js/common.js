var dataForms = {
    scrollToDiv: function (el) {
        $("html, body").animate({ scrollTop: $("#" + el).offset().top }, 1);
    },
    clearSearch: function() {
        $('.search-input').val('');
        $('#searchForm').submit();
    },
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
    reply: function(user){
        pageEvents.scrollBottom(500);
        $('#text').summernote('code', '<b><i>' + user + ',</i></b><span>&nbsp;');
        $('.note-editable').placeCursorAtEnd();
    },
    initSummernote: function (textarea, placeholder, lang, height) {
        var el = $('#' + textarea).summernote({
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['para', ['ul']],
                ['insert', ['link']]
            ],
            shortcuts: false,
            followingToolbar: false,
            minHeight: height,
            placeholder: placeholder,
            disableDragAndDrop:true,
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
                    if (e.keyCode == 9 || e.keyCode == 13) {
                        e.preventDefault();
                        return;
                    }
                    var t = dataForms.stripHtml(e.currentTarget.innerHTML);
                    if (t.length >= $('#' + textarea).attr('maxlength')) {
                        if (e.keyCode != 8 && !(e.keyCode >=37 && e.keyCode <=40) && e.keyCode != 46 && !(e.keyCode == 88 && e.ctrlKey) && !(e.keyCode == 67 && e.ctrlKey) && !(e.keyCode == 65 && e.ctrlKey))
                            e.preventDefault();
                    }
                },
                onChange: function(contents) {
                    var id = textarea + '-counter';
                    $('#' + id).text($('#' + textarea).attr('maxlength') - dataForms.stripHtml(contents).length);
                    var text = document.getElementById(textarea);
                    var form = $(text).closest("form");
                     if ($('#' + textarea).attr('required') && el.summernote('isEmpty')) {
                         text.setCustomValidity("error");
                         if(form.hasClass('was-validated')) {
                             $(text).next('.note-editor').removeClass('success-valid');
                             $(text).next('.note-editor').addClass('error-invalid');
                         }
                     } else {
                         text.setCustomValidity("");
                         if(form.hasClass('was-validated')) {
                             $(text).next('.note-editor').removeClass('error-invalid');
                             $(text).next('.note-editor').addClass('success-valid');
                         }
                     }
                },
                onPaste: function (e) {
                    e.preventDefault();
                    return;
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
    bsSummernoteValidation: function () {
        if ($("form").hasClass('was-validated')) {
            $(".summernote").each(function (i, el) {
                if ($(el).is(":invalid")) {
                    $(el).next('.note-editor').removeClass('success-valid');
                    $(el).next('.note-editor').addClass('error-invalid');
                }
                else {
                    $(el).next('.note-editor').removeClass('error-invalid');
                    $(el).next('.note-editor').addClass('success-valid');
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
            li.innerHTML="<span><i class='fa fa-file file-attachment file-i' aria-hidden='true'></i>"+file.name+"</span>";
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
    scrollMark: false,
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
    scrollBottom: function (timeout) {
        if(!pageEvents.scrollMark) {
            pageEvents.scrollMark = true;
            $("html, body").animate({ scrollTop: $(document).height() }, timeout, function () {
                pageEvents.scrollMark = false;
            });
        }
    }
}

var questions = {
    downloadAttachment: function (file) {
        window.open(file, '_blank');
    },
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
                    $('#showProfileModal .modal-title').text(res.fio);
                    if (res.avatar != undefined) {
                        $('#showProfileModal .img-thumbnail').attr('src', context + '/controller?command=show_image&file=' + res.avatar);
                    } else {
                        $('#showProfileModal .img-thumbnail').attr('src', context + '/static/custom/images/noavatar.png');
                    }
                    $('#showProfileModal .profile-role-span').text(res.role_name);
                    $('#showProfileModal .profile-role-span').removeClass(function (index, className) {
                        return (className.match(/(^|\s)badge-\S+/g) || []).join(' ');
                    });
                    $('#showProfileModal .profile-role-span').addClass('badge-' + res.role_color);
                    $('#showProfileModal #fname').text(res.first_name);
                    $('#showProfileModal #lname').text(res.last_name);
                    $('#showProfileModal #mname').text(res.middle_name);
                    $('#showProfileModal #email').text(res.email);
                    if (res.telegram != undefined && res.telegram.length > 0) {
                        $('#showProfileModal #telegram').html('<a href="' + res.telegram_lnk + '" target="_blank">@' + res.telegram + '</a>');
                    } else {
                        $('#showProfileModal #telegram').empty();
                    }
                    if (res.email != undefined && res.email.length > 0) {
                        $('#showProfileModal #email').html('<a href="mailto:' + res.email + '" target="_blank">' + res.email + '</a>');
                    } else {
                        $('#showProfileModal #email').empty();
                    }
                    $('#showProfileModal #q_count').text(res.q_count);
                    $('#showProfileModal #a_count').text(res.a_count);
                    $('#showProfileModal').modal('show');
                }
            }
        });
    },
    delete: function (ev, context, id, inner) {
        if(!inner) {
            ev.stopPropagation();
        }
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
    },
    changeStatus: function (context, el, id) {
        var close = $(el).is(':checked');
        $(document.body).append('<form action="' + context + '/controller?command=edit_question" method="post" style="display: none" id="deleteForm">' +
            '<input type="hidden" name="id" value="' + id + '">' +
            '<input type="hidden" name="close" value="' + close + '">' +
            '</form>');
        $('#deleteForm').submit();
    }
}

var categories = {
    delete: function (ev, context, id) {
        ev.stopPropagation();
        bootbox.confirm({
            title: message.warn,
            message: message.delete_category,
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
                    $(document.body).append('<form action="' + context + '/controller?command=delete_category" method="post" style="display: none" id="deleteForm">' +
                        '<input type="hidden" name="id" value="' + id + '">' +
                        '</form>');
                    $('#deleteForm').submit();
                }
            }
        });
    }
}

var users = {
    delete: function (ev, context, id) {
        ev.stopPropagation();
        bootbox.confirm({
            title: message.warn,
            message: message.delete_user,
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
                    $(document.body).append('<form action="' + context + '/controller?command=delete_user" method="post" style="display: none" id="deleteForm">' +
                        '<input type="hidden" name="id" value="' + id + '">' +
                        '</form>');
                    $('#deleteForm').submit();
                }
            }
        });
    }
}

var answers = {
    edit: function (id) {
        $('#comment-data-' + id).hide();
        $('#reply-link' + id).hide();
        $('#comment-edit-data-' + id).show();
    },
    delete: function (ev, context, id) {
        bootbox.confirm({
            title: message.warn,
            message: message.delete_answer,
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
                    $(document.body).append('<form action="' + context + '/controller?command=delete_answer" method="post" style="display: none" id="deleteForm">' +
                        '<input type="hidden" name="id" value="' + id + '">' +
                        '</form>');
                    $('#deleteForm').submit();
                }
            }
        });
    },
    like: function (context, id, grade) {
        $.ajax({
            url: context + "/controller?command=change_rating",
            data: {id: id, ajax: true, grade: grade},
            type: "POST",
            dataType: 'json',
            success: function (res) {
                if(res.grade == undefined) {
                    toasts.show("error", message.error, message.wrong_mark);
                    return;
                }
                $('#rating_' + id).text(res.grade);
                $('#rating_' + id).removeClass(function (index, className) {
                    return (className.match(/(^|\s)badge-\S+/g) || []).join(' ');
                });

                if(res.grade > 0) {
                    $('#rating_' + id).addClass('badge-success');
                } else if(res.grade < 0) {
                    $('#rating_' + id).addClass('badge-danger');
                } else {
                    $('#rating_' + id).addClass('badge-secondary');
                }

                if(grade) {
                    $('#like_' + id).removeAttr('onclick').removeClass('like-none').addClass('like-done');
                    $('#like_' + id).html('<i class="fas fa-thumbs-up"></i>');

                    $('#unlike_' + id).attr('onclick', 'answers.like(\'' + context + '\', ' + id + ', false);').removeClass('like-done').addClass('like-none');
                    $('#unlike_' + id).html('<i class="far fa-thumbs-down"></i>');
                } else {
                    $('#unlike_' + id).removeAttr('onclick').removeClass('like-none').addClass('like-done');
                    $('#unlike_' + id).html('<i class="fas fa-thumbs-down"></i>');

                    $('#like_' + id).attr('onclick', 'answers.like(\'' + context + '\', ' + id + ', true);').removeClass('like-done').addClass('like-none');
                    $('#like_' + id).html('<i class="far fa-thumbs-up"></i>');
                }
            }
        });
    },
    markAsSolution: function (context, id, check) {
        var check = $('#solution_' + id).children().hasClass('far');

         $.ajax({
             url: context + "/controller?command=mark_solution",
             data: {id: id, ajax: true, check: check},
             type: "POST",
             dataType: 'json',
             success: function (res) {
                 if(!res.result) {
                     toasts.show("error", message.error, message.wrong_solution);
                     return;
                 }
                $('.solution-link').each(function(i, el) {
                    $(el).html('<i class="far fa-check-square"></i>');
                    $(el).removeClass("solution-mark");
                    $(el).attr('title', $(el).data('def-title'));
                    $(el).attr('data-original-title', $(el).data('def-title'));
                    $(el).tooltip('update');
                    if(!check && $(el).attr('id') == ('solution_' + id)) {
                        $(el).tooltip('show');
                    }
                });
                if(check) {
                    $('#solution_' + id).html('<i class="fas fa-check-square"></i>');
                    $('#solution_' + id).addClass("solution-mark");
                    $('#solution_' + id).attr('title', $('#solution_' + id).data('rm-title'));
                    $('#solution_' + id).attr('data-original-title', $('#solution_' + id).data('rm-title'));
                    $('#solution_' + id).tooltip('update');
                    $('#solution_' + id).tooltip('show');
                }
             }
         });
    }
}

$.fn.extend({
    placeCursorAtEnd: function() {
        if (this.length === 0) {
            throw new Error("Cannot manipulate an element if there is no element!");
        }
        var el = this[0];
        var range = document.createRange();
        var sel = window.getSelection();
        var childLength = el.childNodes.length;
        if (childLength > 0) {
            var lastNode = el.childNodes[childLength - 1];
            var lastNodeChildren = lastNode.childNodes.length;
            range.setStart(lastNode, lastNodeChildren);
            range.collapse(true);
            sel.removeAllRanges();
            sel.addRange(range);
        }
        return this;
    }
});