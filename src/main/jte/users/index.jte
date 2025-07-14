@import org.example.hexlet.dto.users.UsersPage
@param UsersPage page

@template.layout.page(
    content = @`
        <h2>${page.getHeader()}</h2>
        <a href="/users/build">New User</a>
        @for(var user : page.getUsers())
            <p><a href="users/${user.getId()}">${user.getFirstName()} ${user.getLastName()}</a></p>
        @endfor
    `
)
