@import org.example.hexlet.dto.courses.CoursesPage
@import org.example.hexlet.util.NamedRoutes
@param CoursesPage page

@template.layout.page(
    page = page,
    content = @`
		<form action="/courses" method="get">
			<input type="search" name="term" value="${page.getTerm()}" />
			<input type="submit" value="Search" />
		</form>

      @if(!page.isVisited())
        Это сообщение показывается только один раз. Если вы хотите увидеть его снова, сотрите куки
      @endif


      <p><a href="${NamedRoutes.buildCoursePath()}">ДOБАВИТЬ НОВЫЙ КУРС</a></p>

        @if(page.getCourses().isEmpty() && page.getTerm().equals(""))
			<p>Пока не добавлено ни одного курса</p>
		@else
			@for(var course : page.getCourses())
			   <div>
				   <h2><a href="${NamedRoutes.coursePath(course.getId())}">${course.getName()}</a></h2>
				   <p>${course.getDescription()}</p>
			   </div>
			@endfor
		@endif
    `
)
