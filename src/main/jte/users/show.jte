@import org.example.hexlet.dto.users.UserPage
@param UserPage page

@template.layout.page(
    content = @`
		<h2>${page.getUser().getName()}</h2>
		<p>Created at: ${page.getUser().getCreatedAt().toString()} ID: ${page.getUser().getId()}</p>
		<p>${page.getUser().getEmail()}</p>
    `
)
