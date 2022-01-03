(function($) {
  $.extend($.summernote.lang, {
    'be': {
      font: {
        bold: 'Паўтлусты',
        italic: 'Курсіў',
        underline: 'Падкрэслены',
        clear: 'Прыбраць стылі шрыфта',
        height: 'Вышыня лініі',
        name: 'Шрыфт',
        strikethrough: 'Закрэслены',
        subscript: 'Ніжні індэкс',
        superscript: 'Верхні індэкс',
        size: 'Памер шрыфта'
      },
      image: {
        image: 'Малюначак',
        insert: 'Уставіць карцінку',
        resizeFull: 'Аднавіць памер',
        resizeHalf: 'Зменшыць да 50%',
        resizeQuarter: 'Зменшыць да 25%',
        floatLeft: 'Размясціць злева',
        floatRight: 'Размясціць справа',
        floatNone: 'Размяшчэнне па змаўчанні',
        shapeRounded: 'Форма: Закругленая',
        shapeCircle: 'Форма: Круг',
        shapeThumbnail: 'Форма: Мініяцюра',
        shapeNone: 'Форма: Не',
        dragImageHere: 'Перацягнеце сюды карцінку',
        dropImage: 'Перацягнеце карцінку',
        selectFromFiles: 'Выбраць з файлаў',
        maximumFileSize: 'Максімальны памер файла',
        maximumFileSizeError: 'Перавышаны максімальны памер файла',
        url: 'URL карцінкі',
        remove: 'Выдаліць малюнак',
        original: 'Арыгінал'
      },
      video: {
        video: 'Відэа',
        videoLink: 'Спасылка на відэа',
        insert: 'Уставіць відэа',
        url: 'URL відэа',
        providers: '(YouTube, Vimeo, Vine, Instagram, DailyMotion або Youku)'
      },
      link: {
        link: 'Спасылка',
        insert: 'Уставіць спасылку',
        unlink: 'Прыбраць спасылку',
        edit: 'рэдагаваць',
        textToDisplay: 'Які адлюстроўваецца тэкст',
        url: 'URL для пераходу',
        openInNewWindow: 'Адчыняць у новым акне'
      },
      table: {
        table: 'Табліца',
        addRowAbove: 'Дадаць радок вышэй',
        addRowBelow: 'Дадаць радок ніжэй',
        addColLeft: 'Дадаць слупок злева',
        addColRight: 'Дадаць слупок справа',
        delRow: 'Выдаліць радок',
        delCol: 'Выдаліць слупок',
        delTable: 'Выдаліць табліцу'
      },
      hr: {
        insert: 'Уставіць гарызантальную лінію'
      },
      style: {
        style: 'Стыль',
        p: 'Нармальны',
        blockquote: 'Цытата',
        pre: 'Код',
        h1: 'Загаловак 1',
        h2: 'Загаловак 2',
        h3: 'Загаловак 3',
        h4: 'Загаловак 4',
        h5: 'Загаловак 5',
        h6: 'Загаловак 6'
      },
      lists: {
        unordered: 'Маркаваны спіс',
        ordered: 'Нумараваны спіс'
      },
      options: {
        help: 'Дапамога',
        fullscreen: 'На ўвесь экран',
        codeview: 'Зыходны код'
      },
      paragraph: {
        paragraph: 'Параграф',
        outdent: 'Зменшыць водступ',
        indent: 'Павялічыць водступ',
        left: 'Выраўнаваць па левым краі',
        center: 'Выраўнаваць па цэнтры',
        right: 'Выраўнаваць па правым краі',
        justify: 'расцягнуць па шырыні'
      },
      color: {
        recent: 'Апошні колер',
        more: 'Яшчэ колеры',
        background: 'Колер фону',
        foreground: 'Колер шрыфта',
        transparent: 'Празрысты',
        setTransparent: 'Зрабіць празрыстым',
        reset: 'Скід',
        resetToDefault: 'Аднавіць змаўчанні'
      },
      shortcut: {
        shortcuts: 'Спалучэнні клавіш',
        close: 'Зачыніць',
        textFormatting: 'Фарматаванне тэксту',
        action: 'Дзеянне',
        paragraphFormatting: 'Фарматаванне параграфа',
        documentStyle: 'Стыль дакумента',
        extraKeys: 'Дадатковыя камбінацыі'
      },
      help: {
        'insertParagraph': 'Новы параграф',
        'undo': 'Адмяніць апошнюю каманду',
        'redo': 'Паўтарыць апошнюю каманду',
        'tab': 'Tab',
        'untab': 'Untab',
        'bold': 'Усталяваць стыль "Тоўсты"',
        'italic': 'Усталяваць стыль "Нахільны"',
        'underline': 'Усталяваць стыль "Падкрэслены"',
        'strikethrough': 'Усталяваць стыль "Закрэслены"',
        'removeFormat': 'Збіць стылі',
        'justifyLeft': 'Выраўнаваць па левым краі',
        'justifyCenter': 'Выраўнаваць па цэнтры',
        'justifyRight': 'Выраўнаваць па правым краі',
        'justifyFull': 'Расцягнуць на ўсю шырыню',
        'insertUnorderedList': 'Уключыць/адключыць маркіраваны спіс',
        'insertOrderedList': 'Уключыць/адключыць нумараваны спіс',
        'outdent': 'Прыбраць водступ у бягучым параграфе',
        'indent': 'Уставіць водступ у бягучым параграфе',
        'formatPara': 'Фарматаваць бягучы блок як параграф (тэг P)',
        'formatH1': 'Фарматаваць бягучы блок як H1',
        'formatH2': 'Фарматаваць бягучы блок як H2',
        'formatH3': 'Фарматаваць бягучы блок як H3',
        'formatH4': 'Фарматаваць бягучы блок як H4',
        'formatH5': 'Фарматаваць бягучы блок як H5',
        'formatH6': 'Фарматаваць бягучы блок як H6',
        'insertHorizontalRule': 'Уставіць гарызантальную рысу',
        'linkDialog.show': 'Паказаць дыялог "Спасылка"'
      },
      history: {
        undo: 'Адмяніць',
        redo: 'Паўтор'
      },
      specialChar: {
        specialChar: 'SPECIAL CHARACTERS',
        select: 'Select Special characters'
      }
    }
  });
})(jQuery);
