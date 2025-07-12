@import org.example.hexlet.dto.courses.CoursesPage
@param CoursesPage page

<html>
    <head>
        <title>Хекслет</title>
    </head>
    <body>
        @if(page.getCourses().isEmpty())
            <p>Пока не добавлено ни одного курса</p>
        @else
            @for(var course : page.getCourses())
               <div>
                   <h2><a href="/courses/${course.getId()}">${course.getName()}</a></h2>
                   <p>${course.getDescription()}</p>
               </div>
            @endfor
        @endif
    </body>
</html>
