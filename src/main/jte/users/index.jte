@import org.example.hexlet.dto.users.UsersPage
@param UsersPage page

@template.layout.page(
    content = @`
        <h2>${page.getHeader()}</h2>
        <h3>Search form</h3>
		<form action="/users" method="get">
			<input type="search" name="term" value="${page.getTerm()}" />
			<input type="submit" value="Search" />
		</form>

		<h3>Create new user</h3>
        <a href="/users/build">New User</a>

        <h3>List of users</h3>
        @if(page.getUsers().isEmpty())
			<p>Пока не добавлено ни одного юзера</p>
		@else
			@for(var user : page.getUsers())
				<p><a href="users/${user.getId()}">${user.getName()}</a></p>
			@endfor
		@endif
    `
)