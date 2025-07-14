@import org.example.hexlet.dto.courses.CoursePage
@param CoursePage page

@template.layout.page(
    content = @`
		<h2>${page.getCourse().getName()}</h2>
		<p>${page.getCourse().getDescription()}</p>
    `
)
