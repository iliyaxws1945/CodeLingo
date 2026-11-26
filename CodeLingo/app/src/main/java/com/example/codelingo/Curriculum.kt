package com.example.codelingo

enum class SkillLevel(val title: String, val subtitle: String, val badgeColorHex: Long) {
    JUNIOR("JUNIOR", "Базовый синтаксис и фундамент", 0xFF58CC02),
    MIDDLE("MIDDLE", "Структуры данных, ООП и практика", 0xFF1CB0F6),
    SENIOR("SENIOR", "Архитектура, память и оптимизация", 0xFFCE82FF)
}

enum class QuestionType {
    MULTIPLE_CHOICE,
    TOKEN_BUILDER,
    FILL_BLANK
}

data class TheorySlide(
    val title: String,
    val explanation: String,
    val codeExample: String,
    val proTip: String
)

data class Question(
    val id: String,
    val type: QuestionType,
    val prompt: String,
    val codeSnippet: String? = null,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val correctTokens: List<String> = emptyList(),
    val explanation: String
)

data class Lesson(
    val id: String,
    val level: SkillLevel,
    val title: String,
    val subtitle: String,
    val icon: String,
    val xpReward: Int,
    val theorySlides: List<TheorySlide>,
    val questions: List<Question>
)

data class Course(
    val id: String,
    val name: String,
    val icon: String,
    val tagline: String,
    val lessons: List<Lesson>
)

object CurriculumRepository {
    val courses = listOf(
        // ==================== PYTHON ====================
        Course(
            id = "python",
            name = "Python",
            icon = "🐍",
            tagline = "Backend, AI, автоматизация и скрипты",
            lessons = listOf(
                Lesson(
                    id = "py_jr",
                    level = SkillLevel.JUNIOR,
                    title = "Синтаксис, переменные и ввод",
                    subtitle = "print(), типы, if/elif",
                    icon = "🌱",
                    xpReward = 25,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Переменные и вывод в Python",
                            explanation = "В Python не нужно указывать тип переменной вручную — интерпретатор определяет его сам. Для вывода в консоль используется функция print(), а для получения длины списка или строки — len().",
                            codeExample = "name = \"Alex\"\npi = 3.14        # float\nnums = [1, 2, 3]\nprint(name)\nprint(len(nums)) # 3",
                            proTip = "Точка с запятой в конце строк не нужна, а вложенные блоки отделяются 4 пробелами."
                        ),
                        TheorySlide(
                            title = "Условия if / elif / else",
                            explanation = "Для проверки нескольких условий подряд в Python используется сокращённое ключевое слово elif (вместо else if). После условия обязательно ставится двоеточие.",
                            codeExample = "score = 85\nif score >= 90:\n    print(\"A\")\nelif score >= 75:\n    print(\"B\")\nelse:\n    print(\"C\")",
                            proTip = "Забытое двоеточие в конце if/elif/for — самая частая ошибка новичков."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "py_jr_1",
                            type = QuestionType.TOKEN_BUILDER,
                            prompt = "Собери команду, которая выведет текст Hello в консоль:",
                            options = listOf("print", "(", "\"Hello\"", ")", "echo", ";"),
                            correctAnswer = "print(\"Hello\")",
                            correctTokens = listOf("print", "(", "\"Hello\"", ")"),
                            explanation = "Как мы только что разобрали, вывод в Python делает функция print() без точки с запятой."
                        ),
                        Question(
                            id = "py_jr_2",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Дополни ветвление для проверки второго условия:",
                            codeSnippet = "if x > 0:\n    print(\"+\")\n___ x < 0:\n    print(\"-\")",
                            options = listOf("elif", "else if", "elseif", "when"),
                            correctAnswer = "elif",
                            explanation = "В Python «иначе если» всегда пишется одним словом — elif."
                        ),
                        Question(
                            id = "py_jr_3",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Какая встроенная функция вернет количество элементов в списке?",
                            options = listOf("size()", "length()", "len()", "count()"),
                            correctAnswer = "len()",
                            explanation = "Функция len() работает со списками, строками, словарями и множествами."
                        )
                    )
                ),
                Lesson(
                    id = "py_mid",
                    level = SkillLevel.MIDDLE,
                    title = "Генераторы списков и функции",
                    subtitle = "List Comprehension, *args, dict",
                    icon = "⚙️",
                    xpReward = 40,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "List Comprehension",
                            explanation = "Вместо длинного цикла for с append() Middle-разработчики используют генераторы списков — компактную запись создания нового списка в одну строку.",
                            codeExample = "# Квадраты чётных чисел:\nsquares = [x * x for x in range(10) if x % 2 == 0]",
                            proTip = "List comprehension работает быстрее обычного цикла с .append() за счет оптимизации внутри CPython."
                        ),
                        TheorySlide(
                            title = "Безопасное чтение словаря и *args",
                            explanation = "Обращение d[\"key\"] выбросит KeyError, если ключа нет. Метод d.get(\"key\", default) безопасно вернет значение по умолчанию. А *args позволяет функции принимать любое число позиционных аргументов.",
                            codeExample = "user = {\"name\": \"Bob\"}\nage = user.get(\"age\", 18)\n\ndef total(*args):\n    return sum(args)",
                            proTip = "Используй .get() всегда, когда данные приходят из внешнего API или JSON."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "py_mid_1",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Безопасно получи значение ключа 'role' (или 'guest', если его нет):",
                            codeSnippet = "role = user.___(\"role\", \"guest\")",
                            options = listOf("get", "find", "fetch", "read"),
                            correctAnswer = "get",
                            explanation = "Метод .get(key, default) не вызывает ошибку KeyError при отсутствии ключа."
                        ),
                        Question(
                            id = "py_mid_2",
                            type = QuestionType.TOKEN_BUILDER,
                            prompt = "Собери list comprehension, который удваивает элементы списка nums:",
                            options = listOf("[", "x * 2", "for", "x", "in", "nums", "]"),
                            correctAnswer = "[x * 2 for x in nums]",
                            correctTokens = listOf("[", "x * 2", "for", "x", "in", "nums", "]"),
                            explanation = "Синтаксис генератора: [выражение for элемент in коллекция]."
                        ),
                        Question(
                            id = "py_mid_3",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Как принять произвольное количество позиционных аргументов в функции?",
                            options = listOf("def f(args[]):", "def f(*args):", "def f(...args):", "def f(&args):"),
                            correctAnswer = "def f(*args):",
                            explanation = "Звёздочка *args собирает все переданные позиционные аргументы в кортеж."
                        )
                    )
                ),
                Lesson(
                    id = "py_sr",
                    level = SkillLevel.SENIOR,
                    title = "Генераторы, Декораторы и GIL",
                    subtitle = "yield, @decorator, async/GIL",
                    icon = "👑",
                    xpReward = 60,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Ленивые генераторы (yield)",
                            explanation = "Если вернуть список из миллиона элементов, он займет сотни мегабайт RAM. Ключевое слово yield превращает функцию в генератор: он выдает элементы по одному и хранит в памяти только текущее состояние.",
                            codeExample = "def read_lines(path):\n    with open(path) as f:\n        for line in f:\n            yield line.strip()",
                            proTip = "Генераторы критичны в data-pipeline и при обработке больших файлов."
                        ),
                        TheorySlide(
                            title = "GIL и многопоточность в CPython",
                            explanation = "Global Interpreter Lock (GIL) позволяет одновременно выполнять байткод Python только одному потоку. Поэтому для CPU-bound задач используют multiprocessing (процессы), а для I/O-bound (сеть, файлы) — asyncio или threading.",
                            codeExample = "# CPU-задачи -> multiprocessing\n# Сеть/БД    -> asyncio (async/await)",
                            proTip = "На собеседованиях на Senior вопрос про GIL и выбор между asyncio и multiprocessing задают почти всегда."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "py_sr_1",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Преврати функцию в ленивый генератор, возвращающий числа по одному:",
                            codeSnippet = "def counter(n):\n    for i in range(n):\n        ___ i",
                            options = listOf("yield", "return", "emit", "next"),
                            correctAnswer = "yield",
                            explanation = "yield приостанавливает выполнение функции и возвращает очередное значение без загрузки всего списка в память."
                        ),
                        Question(
                            id = "py_sr_2",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Что лучше выбрать в CPython для тяжёлых математических вычислений (CPU-bound) на всех ядрах?",
                            options = listOf("threading", "asyncio", "multiprocessing", "yield"),
                            correctAnswer = "multiprocessing",
                            explanation = "Из-за GIL потоки (threading) не ускоряют CPU-bound код, а отдельные процессы (multiprocessing) работают параллельно на разных ядрах."
                        )
                    )
                )
            )
        ),

        // ==================== C++ ====================
        Course(
            id = "cpp",
            name = "C++",
            icon = "⚙️",
            tagline = "Игровые движки, HFT, системное ПО",
            lessons = listOf(
                Lesson(
                    id = "cpp_jr",
                    level = SkillLevel.JUNIOR,
                    title = "Потоки ввода/вывода и типы",
                    subtitle = "<iostream>, std::cout, типы",
                    icon = "🌱",
                    xpReward = 25,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Первая программа на C++",
                            explanation = "Чтобы выводить текст в консоль, нужно подключить заголовок <iostream>. Вывод осуществляется через поток std::cout и оператор <<, а ввод — через std::cin и оператор >>.",
                            codeExample = "#include <iostream>\n\nint main() {\n    std::cout << \"Hello C++\";\n    return 0;\n}",
                            proTip = "Каждая инструкция в C++ обязательно заканчивается точкой с запятой."
                        ),
                        TheorySlide(
                            title = "Строгая типизация и auto",
                            explanation = "В C++ у каждой переменной есть строгий тип (int, double, std::string). Ключевое слово auto позволяет компилятору вывести тип автоматически при инициализации.",
                            codeExample = "int speed = 120;\nauto ratio = 3.14; // double",
                            proTip = "auto выводится на этапе компиляции — потери производительности нет."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "cpp_jr_1",
                            type = QuestionType.TOKEN_BUILDER,
                            prompt = "Выведи строку Hi в стандартный поток вывода C++:",
                            options = listOf("std::cout", "<<", "\"Hi\"", ";", ">>", "print"),
                            correctAnswer = "std::cout << \"Hi\";",
                            correctTokens = listOf("std::cout", "<<", "\"Hi\"", ";"),
                            explanation = "Оператор << направляет данные в поток std::cout."
                        ),
                        Question(
                            id = "cpp_jr_2",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Подключи заголовок для работы с std::cout и std::cin:",
                            codeSnippet = "#include <___>\nint main() { return 0; }",
                            options = listOf("iostream", "stdio.h", "vector", "memory"),
                            correctAnswer = "iostream",
                            explanation = "<iostream> содержит стандартные потоки ввода и вывода."
                        )
                    )
                ),
                Lesson(
                    id = "cpp_mid",
                    level = SkillLevel.MIDDLE,
                    title = "Ссылки, STL и Умные указатели",
                    subtitle = "const&, std::vector, unique_ptr",
                    icon = "🧭",
                    xpReward = 40,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Передача по константной ссылке",
                            explanation = "Передача больших объектов (например, std::vector или std::string) по значению копирует всю память. Передача по const T& избегает копирования и запрещает случайно изменить объект.",
                            codeExample = "void printSize(const std::vector<int>& v) {\n    std::cout << v.size();\n}",
                            proTip = "Правило Middle C++: передавай тяжелые объекты по const&, а примитивы (int, double) — по значению."
                        ),
                        TheorySlide(
                            title = "RAII и std::unique_ptr",
                            explanation = "Ручной new/delete легко приводит к утечкам памяти. В современном C++ используют умные указатели из <memory>: std::unique_ptr сам освобождает память при выходе из области видимости.",
                            codeExample = "auto ptr = std::make_unique<int>(42);\n// delete не нужен — память очистится сама!",
                            proTip = "std::unique_ptr имеет нулевой оверхед по сравнению с сырым указателем."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "cpp_mid_1",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Передай строку s в функцию без копирования и без права изменения:",
                            codeSnippet = "void log(___ std::string___ s) { ... }",
                            options = listOf("const ... &", "static ... *", "auto ... &", "const ... *"),
                            correctAnswer = "const ... &",
                            explanation = "const std::string& гарантирует отсутствие копирования и защиту от модификации."
                        ),
                        Question(
                            id = "cpp_mid_2",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Какой умный указатель обеспечивает единоличное владение ресурсом с нулевым оверхедом?",
                            options = listOf("std::shared_ptr", "std::unique_ptr", "std::weak_ptr", "raw pointer"),
                            correctAnswer = "std::unique_ptr",
                            explanation = "std::unique_ptr нельзя скопировать, только переместить (std::move), и он не тратит память на счётчик ссылок."
                        )
                    )
                ),
                Lesson(
                    id = "cpp_sr",
                    level = SkillLevel.SENIOR,
                    title = "Move-семантика и Оптимизация",
                    subtitle = "std::move, rvalue &&, cache locality",
                    icon = "👑",
                    xpReward = 60,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Move-семантика и rvalue-ссылки (&&)",
                            explanation = "Вместо глубокого копирования буфера std::move приводит объект к rvalue (T&&), позволяя «украсть» внутренний указатель на память за O(1) и обнулить исходный объект.",
                            codeExample = "std::vector<int> a(1000000);\nstd::vector<int> b = std::move(a);\n// Копирования миллиона int не было!",
                            proTip = "После std::move(a) объект a находится в валидном, но неопределенном состоянии — его можно только перезаписать или уничтожить."
                        ),
                        TheorySlide(
                            title = "Cache Locality: vector vs list",
                            explanation = "std::vector хранит элементы непрерывно в памяти, поэтому процессор эффективно подгружает их в L1/L2 кэш. std::list разбрасывает узлы по куче, вызывая постоянные cache miss.",
                            codeExample = "// Почти всегда быстрее даже при частых проходах:\nstd::vector<int> data;\ndata.reserve(1000); // без реаллокаций",
                            proTip = "Вызов .reserve(N) заранее избавляет от лишних перевыделений памяти при push_back."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "cpp_sr_1",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Перемести владение вектором a в b без копирования элементов:",
                            codeSnippet = "std::vector<int> b = std::___(a);",
                            options = listOf("move", "copy", "swap", "forward"),
                            correctAnswer = "move",
                            explanation = "std::move кастует объект к rvalue-ссылке, вызывая конструктор перемещения."
                        ),
                        Question(
                            id = "cpp_sr_2",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Почему std::vector обычно быстрее std::list при последовательном обходе?",
                            options = listOf(
                                "У list нет итераторов",
                                "Элементы vector лежат непрерывно в памяти (CPU cache locality)",
                                "vector хранится только на стеке",
                                "vector не использует указатели"
                            ),
                            correctAnswer = "Элементы vector лежат непрерывно в памяти (CPU cache locality)",
                            explanation = "Непрерывное расположение данных в памяти позволяет кэш-префетчеру CPU предсказывать чтение без промахов."
                        )
                    )
                )
            )
        ),

        // ==================== JAVA ====================
        Course(
            id = "java",
            name = "Java",
            icon = "☕",
            tagline = "Enterprise Backend, Android, банкинг",
            lessons = listOf(
                Lesson(
                    id = "java_jr",
                    level = SkillLevel.JUNIOR,
                    title = "Основы, классы и строки",
                    subtitle = "println, final, equals()",
                    icon = "🌱",
                    xpReward = 25,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Вывод и неизменяемость (final)",
                            explanation = "В Java весь код живет внутри классов. Вывод в консоль с переносом строки делает System.out.println(). Чтобы сделать переменную константой, используется модификатор final.",
                            codeExample = "final int MAX_USERS = 100;\nSystem.out.println(MAX_USERS);",
                            proTip = "Попытка изменить final-переменную приведет к ошибке ещё на этапе компиляции."
                        ),
                        TheorySlide(
                            title = "Сравнение строк: equals() vs ==",
                            explanation = "String в Java — это объект. Оператор == сравнивает ссылки в памяти (один ли это объект), а метод .equals() сравнивает само содержимое текста.",
                            codeExample = "String a = new String(\"hi\");\nString b = new String(\"hi\");\na == b       // false!\na.equals(b)  // true!",
                            proTip = "Всегда сравнивай строки через .equals(), иначе поймаешь хитрый баг в продакшене."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "java_jr_1",
                            type = QuestionType.TOKEN_BUILDER,
                            prompt = "Собери вывод строки Java в консоль:",
                            options = listOf("System.out.println", "(", "\"Java\"", ")", ";", "print"),
                            correctAnswer = "System.out.println(\"Java\");",
                            correctTokens = listOf("System.out.println", "(", "\"Java\"", ")", ";"),
                            explanation = "System.out.println() — стандартный способ вывода в Java."
                        ),
                        Question(
                            id = "java_jr_2",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Правильно сравни текст двух строк a и b:",
                            codeSnippet = "if (a.___(b)) {\n    System.out.println(\"Match\");\n}",
                            options = listOf("equals", "==", "same", "match"),
                            correctAnswer = "equals",
                            explanation = "Метод .equals() сравнивает символы строк, а не адреса в памяти."
                        )
                    )
                ),
                Lesson(
                    id = "java_mid",
                    level = SkillLevel.MIDDLE,
                    title = "Коллекции, Stream API и Optional",
                    subtitle = "HashMap, Stream.filter, Optional",
                    icon = "📚",
                    xpReward = 40,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Stream API",
                            explanation = "Начиная с Java 8 коллекции обрабатывают декларативно через Stream API: filter() отбирает элементы, map() преобразует их, а toList() собирает результат.",
                            codeExample = "List<Integer> evens = nums.stream()\n    .filter(n -> n % 2 == 0)\n    .toList();",
                            proTip = "Стримы ленивы: промежуточные операции (filter, map) не выполняются, пока не вызвана терминальная операция (toList, count, forEach)."
                        ),
                        TheorySlide(
                            title = "Защита от NullPointerException: Optional",
                            explanation = "Optional<T> — контейнер, который явно показывает, что значение может отсутствовать. Вместо проверки if (x != null) используют .orElse() или .orElseThrow().",
                            codeExample = "String name = findUser(id)\n    .map(User::getName)\n    .orElse(\"Anonymous\");",
                            proTip = "Не используй Optional как поле класса или параметр метода — он создан для возвращаемых значений."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "java_mid_1",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Отфильтруй в стриме только положительные числа:",
                            codeSnippet = "nums.stream().___(n -> n > 0).toList();",
                            options = listOf("filter", "where", "select", "find"),
                            correctAnswer = "filter",
                            explanation = "Метод .filter(Predicate) оставляет в потоке только элементы, удовлетворяющие условию."
                        ),
                        Question(
                            id = "java_mid_2",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Какой метод Optional вернет запасное значение, если внутри пусто?",
                            options = listOf("getOrDefault()", "orElse()", "fallback()", "ifNull()"),
                            correctAnswer = "orElse()",
                            explanation = "Optional.orElse(defaultValue) безопасно возвращает значение по умолчанию при отсутствии объекта."
                        )
                    )
                ),
                Lesson(
                    id = "java_sr",
                    level = SkillLevel.SENIOR,
                    title = "JVM, GC и Конкурентность",
                    subtitle = "equals/hashCode, volatile, Virtual Threads",
                    icon = "👑",
                    xpReward = 60,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Контракт equals() и hashCode()",
                            explanation = "Если два объекта равны по equals(), их hashCode() ОБЯЗАН быть одинаковым. Если переопределить equals() без hashCode(), объект «потеряется» внутри HashMap или HashSet.",
                            codeExample = "// Если a.equals(b) == true,\n// то a.hashCode() == b.hashCode() ВСЕГДА!",
                            proTip = "В современной Java используй record — компилятор сам сгенерирует правильные equals(), hashCode() и toString()."
                        ),
                        TheorySlide(
                            title = "Многопоточность: volatile и Virtual Threads",
                            explanation = "Ключевое слово volatile гарантирует видимость изменений переменной между потоками (чтение/запись мимо кэша ядра). А Virtual Threads (Java 21) позволяют запускать миллионы лёгких потоков для I/O-задач.",
                            codeExample = "private volatile boolean running = true;\n// Java 21:\nThread.startVirtualThread(() -> handleRequest());",
                            proTip = "volatile гарантирует видимость (visibility), но НЕ атомарность операции i++ — для счетчиков бери AtomicInteger."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "java_sr_1",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Что сломается, если переопределить equals() без hashCode()?",
                            options = listOf(
                                "Код не скомпилируется",
                                "Поиск объектов в HashMap и HashSet",
                                "Работа сборщика мусора GC",
                                "Оператор instanceof"
                            ),
                            correctAnswer = "Поиск объектов в HashMap и HashSet",
                            explanation = "HashMap сначала ищет корзину (bucket) по hashCode(). Если хэши разные, equals() даже не вызовется."
                        ),
                        Question(
                            id = "java_sr_2",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Гарантируй видимость флага stop между потоками без полной блокировки:",
                            codeSnippet = "private ___ boolean stop = false;",
                            options = listOf("volatile", "transient", "synchronized", "final"),
                            correctAnswer = "volatile",
                            explanation = "volatile запрещает кэширование переменной локально в потоке, обеспечивая актуальное чтение из основной памяти."
                        )
                    )
                )
            )
        ),

        // ==================== JAVASCRIPT ====================
        Course(
            id = "js",
            name = "JavaScript",
            icon = "🟨",
            tagline = "Frontend, Node.js, Fullstack веб",
            lessons = listOf(
                Lesson(
                    id = "js_jr",
                    level = SkillLevel.JUNIOR,
                    title = "let/const, вывод и строгое сравнение",
                    subtitle = "const, ===, console.log",
                    icon = "🌱",
                    xpReward = 25,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Переменные: const и let",
                            explanation = "В современном JS забудь про var. Используй const по умолчанию (переменную нельзя переназначить) и let — если значение будет меняться. Для отладочного вывода используется console.log().",
                            codeExample = "const appName = \"CodeLingo\";\nlet score = 0;\nscore += 10;\nconsole.log(appName, score);",
                            proTip = "const защищает ссылку от переназначения, но свойства объекта или элементы массива внутри const менять можно."
                        ),
                        TheorySlide(
                            title = "Строгое равенство ===",
                            explanation = "Оператор == пытается привести типы (поэтому 5 == \"5\" это true). Всегда используй строгое равенство ===, которое проверяет и значение, и тип данных.",
                            codeExample = "5 == \"5\"   // true (опасно!)\n5 === \"5\"  // false (правильно)",
                            proTip = "В профессиональной разработке == практически запрещён линтерами."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "js_jr_1",
                            type = QuestionType.TOKEN_BUILDER,
                            prompt = "Выведи строку OK в консоль:",
                            options = listOf("console.log", "(", "\"OK\"", ")", ";", "print"),
                            correctAnswer = "console.log(\"OK\");",
                            correctTokens = listOf("console.log", "(", "\"OK\"", ")", ";"),
                            explanation = "console.log() выводит переданные значения в консоль браузера или Node.js."
                        ),
                        Question(
                            id = "js_jr_2",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Используй строгое сравнение без неявного приведения типов:",
                            codeSnippet = "if (userId ___ 42) {\n    console.log(\"Admin\");\n}",
                            options = listOf("===", "==", "=", ":="),
                            correctAnswer = "===",
                            explanation = "=== проверяет точное совпадение и значения, и типа."
                        )
                    )
                ),
                Lesson(
                    id = "js_mid",
                    level = SkillLevel.MIDDLE,
                    title = "Стрелочные функции, Массивы и Async",
                    subtitle = "=>, map/filter, async/await",
                    icon = "🏹",
                    xpReward = 40,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Стрелочные функции и .map()",
                            explanation = "Стрелочная функция (x) => x * 2 короче обычной и не создает собственный this. В паре с методами массивов .map() и .filter() это основа функционального стиля в JS.",
                            codeExample = "const prices = [10, 20, 30];\nconst doubled = prices.map(p => p * 2);\n// [20, 40, 60]",
                            proTip = ".map() всегда возвращает новый массив той же длины, не изменяя исходный."
                        ),
                        TheorySlide(
                            title = "Асинхронность: async / await",
                            explanation = "Запросы к серверу (fetch) возвращают Promise. Внутри async-функции ключевое слово await «приостанавливает» выполнение функции до получения ответа, делая код читаемым как синхронный.",
                            codeExample = "async function loadUser() {\n    const res = await fetch(\"/api/user\");\n    return await res.json();\n}",
                            proTip = "Оборачивай await в try { ... } catch (err) { ... } для обработки сетевых ошибок."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "js_mid_1",
                            type = QuestionType.TOKEN_BUILDER,
                            prompt = "Собери стрелочную функцию, умножающую x на 2:",
                            options = listOf("const", "dbl", "=", "(x)", "=>", "x * 2"),
                            correctAnswer = "const dbl = (x) => x * 2",
                            correctTokens = listOf("const", "dbl", "=", "(x)", "=>", "x * 2"),
                            explanation = "Если тело стрелочной функции состоит из одного выражения без фигурных скобок, return подставляется неявно."
                        ),
                        Question(
                            id = "js_mid_2",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Дождись завершения Promise внутри асинхронной функции:",
                            codeSnippet = "async function getData() {\n    const res = ___ fetch(\"/data\");\n}",
                            options = listOf("await", "wait", "yield", "then"),
                            correctAnswer = "await",
                            explanation = "await работает внутри async-функций и распаковывает результат Promise."
                        )
                    )
                ),
                Lesson(
                    id = "js_sr",
                    level = SkillLevel.SENIOR,
                    title = "Event Loop, Microtasks и Замыкания",
                    subtitle = "Call Stack, Promise vs setTimeout, Closures",
                    icon = "👑",
                    xpReward = 60,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Event Loop: Microtasks vs Macrotasks",
                            explanation = "JavaScript однопоточен. Когда стек вызовов пустеет, Event Loop сначала очищает ВСЮ очередь микрозадач (Promise.then, queueMicrotask), и только потом берет одну макрозадачу (setTimeout, события DOM).",
                            codeExample = "setTimeout(() => console.log(\"1\"), 0);\nPromise.resolve().then(() => console.log(\"2\"));\nconsole.log(\"3\");\n// Вывод: 3, 2, 1",
                            proTip = "Понимание порядка выполнения Event Loop — главный тест на Senior JS."
                        ),
                        TheorySlide(
                            title = "Замыкания (Closures)",
                            explanation = "Замыкание — это способность функции «помнить» и сохранять доступ к переменным из внешней области видимости даже после того, как внешняя функция завершила работу.",
                            codeExample = "function makeCounter() {\n    let count = 0;\n    return () => ++count;\n}\nconst next = makeCounter();\nnext(); // 1, затем 2",
                            proTip = "Замыкания используют для инкапсуляции приватного состояния и в хуках React."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "js_sr_1",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Что выведется первым: callback из setTimeout(..., 0) или из Promise.resolve().then(...)?",
                            options = listOf(
                                "setTimeout",
                                "Promise.then (микрозадача)",
                                "Зависит от браузера",
                                "Они выполнятся одновременно"
                            ),
                            correctAnswer = "Promise.then (микрозадача)",
                            explanation = "Очередь микрозадач (Promise.then) всегда имеет приоритет над очередью макрозадач (setTimeout)."
                        ),
                        Question(
                            id = "js_sr_2",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Как называется механизм, благодаря которому внутренняя функция сохраняет доступ к переменным внешней?",
                            options = listOf("Hoisting", "Closure (Замыкание)", "Prototype", "Currying"),
                            correctAnswer = "Closure (Замыкание)",
                            explanation = "Замыкание (Closure) привязывает лексическое окружение внешней функции к возвращаемой внутренней функции."
                        )
                    )
                )
            )
        ),

        // ==================== C ====================
        Course(
            id = "c",
            name = "C",
            icon = "🔧",
            tagline = "Ядра ОС, микроконтроллеры, embedded",
            lessons = listOf(
                Lesson(
                    id = "c_jr",
                    level = SkillLevel.JUNIOR,
                    title = "Основы, printf и адреса (&)",
                    subtitle = "stdio.h, %d, scanf",
                    icon = "🌱",
                    xpReward = 25,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Форматированный вывод printf()",
                            explanation = "В языке C функция printf() из <stdio.h> подставляет переменные в строку через спецификаторы формата: %d — целое число (int), %f — дробное (float), %s — строка.",
                            codeExample = "#include <stdio.h>\nint age = 20;\nprintf(\"Age: %d\\n\", age);",
                            proTip = "Несоответствие спецификатора и типа переменной в printf приводит к Undefined Behavior."
                        ),
                        TheorySlide(
                            title = "Ввод через scanf() и оператор адреса &",
                            explanation = "Чтобы функция scanf() могла записать введенное пользователем значение прямо в вашу переменную, ей нужно передать адрес этой переменной в памяти с помощью оператора &.",
                            codeExample = "int num;\nscanf(\"%d\", &num);",
                            proTip = "Для строк (char str[50]) знак & в scanf не ставят, потому что имя массива уже является адресом его первого элемента."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "c_jr_1",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Подставь правильный спецификатор формата для вывода целого int:",
                            codeSnippet = "int score = 95;\nprintf(\"Score: ___\", score);",
                            options = listOf("%d", "%f", "%s", "%c"),
                            correctAnswer = "%d",
                            explanation = "%d (signed decimal integer) выводит переменные типа int."
                        ),
                        Question(
                            id = "c_jr_2",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Передай адрес переменной x в функцию scanf:",
                            codeSnippet = "int x;\nscanf(\"%d\", ___x);",
                            options = listOf("&", "*", "->", "%"),
                            correctAnswer = "&",
                            explanation = "Оператор & берет адрес переменной в памяти."
                        )
                    )
                ),
                Lesson(
                    id = "c_mid",
                    level = SkillLevel.MIDDLE,
                    title = "Указатели, Куча и C-строки",
                    subtitle = "malloc/free, '\\0', sizeof",
                    icon = "💾",
                    xpReward = 40,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Динамическая память: malloc и free",
                            explanation = "В C нет сборщика мусора. Память в куче выделяется функцией malloc(размер_в_байтах) с помощью оператора sizeof, а после использования обязательно освобождается через free().",
                            codeExample = "int *arr = malloc(10 * sizeof(int));\nif (arr != NULL) {\n    arr[0] = 42;\n    free(arr);\n}",
                            proTip = "Всегда проверяй указатель на NULL после malloc и обнуляй его после free (arr = NULL), чтобы избежать висячих указателей."
                        ),
                        TheorySlide(
                            title = "Строки в C и нуль-терминатор '\\0'",
                            explanation = "В языке C нет встроенного типа string — строка это массив char[], заканчивающийся байтом '\\0'. Без него функции strlen и printf будут читать чужую память дальше!",
                            codeExample = "char word[4] = {'C', 'a', 't', '\\0'};\n// Размер буфера = длина слова + 1 байт!",
                            proTip = "Всегда закладывай +1 байт под '\\0' при выделении буфера под строку."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "c_mid_1",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Какая функция освобождает память, выделенную через malloc()?",
                            options = listOf("delete()", "free()", "release()", "clean()"),
                            correctAnswer = "free()",
                            explanation = "free(ptr) возвращает блок памяти обратно аллокатору."
                        ),
                        Question(
                            id = "c_mid_2",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Какой символ отмечает конец C-строки в памяти?",
                            options = listOf("'\\n'", "'\\0'", "'EOF'", "' '"),
                            correctAnswer = "'\\0'",
                            explanation = "Нулевой байт '\\0' (NUL) служит маркером конца строки."
                        )
                    )
                ),
                Lesson(
                    id = "c_sr",
                    level = SkillLevel.SENIOR,
                    title = "Битовые операции, Выравнивание и UB",
                    subtitle = "Struct Padding, volatile, Undefined Behavior",
                    icon = "👑",
                    xpReward = 60,
                    theorySlides = listOf(
                        TheorySlide(
                            title = "Выравнивание структур (Struct Padding)",
                            explanation = "Компилятор выравнивает поля struct по адресам, кратным их размеру, вставляя пустые байты (padding). Поэтому порядок полей влияет на итоговый sizeof(struct)!",
                            codeExample = "struct Bad  { char a; int b; char c; }; // 12 байт!\nstruct Good { int b; char a; char c; }; // 8 байт!",
                            proTip = "Располагай поля структуры от самых больших типов к самым маленьким, чтобы экономить RAM."
                        ),
                        TheorySlide(
                            title = "Ключевое слово volatile в Embedded",
                            explanation = "Компилятор может закэшировать чтение переменной в регистре CPU. Модификатор volatile запрещает эту оптимизацию — это критично при чтении регистров железа (MMIO) или флагов из прерываний (ISR).",
                            codeExample = "volatile uint32_t *reg = (uint32_t*)0x40020000;\nwhile ((*reg & 1) == 0) { /* ждем бит железа */ }",
                            proTip = "Без volatile компилятор может превратить цикл ожидания аппаратного регистра в бесконечный цикл."
                        )
                    ),
                    questions = listOf(
                        Question(
                            id = "c_sr_1",
                            type = QuestionType.MULTIPLE_CHOICE,
                            prompt = "Как уменьшить размер struct в памяти за счет снижения padding?",
                            options = listOf(
                                "Размещать поля от большего размера к меньшему",
                                "Использовать только char",
                                "Перенести struct в main()",
                                "Добавить static к полям"
                            ),
                            correctAnswer = "Размещать поля от большего размера к меньшему",
                            explanation = "Сортировка полей по убыванию размера минимизирует пустые промежутки выравнивания."
                        ),
                        Question(
                            id = "c_sr_2",
                            type = QuestionType.FILL_BLANK,
                            prompt = "Запрети компилятору кэшировать чтение аппаратного регистра:",
                            codeSnippet = "___ unsigned int *status_reg = (unsigned int*)0x4000;",
                            options = listOf("volatile", "register", "extern", "const"),
                            correctAnswer = "volatile",
                            explanation = "volatile заставляет CPU каждый раз обращаться непосредственно к адресу в памяти."
                        )
                    )
                )
            )
        )
    )
}
