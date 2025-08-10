@import org.example.hexlet.dto.MainPage
@import org.example.hexlet.util.NamedRoutes
@param MainPage page

<!-- Изменения HTML в шаблоне не требуют перезапуска сервера -->
@template.layout.page(
    content = @`
    	<h2 class="text-body-emphasis">Привет, Хекслет!</h2>
    	<p>Javalin + jte</p>
        @if(page.getCurrentUser() != null)
            Добро пожаловать, ${page.getCurrentUser()}.
            Чтобы разлогиниться, удалите куку JSESSIONID из браузера
        @else
            <a href="${NamedRoutes.sessionBuildPath()}">Залогиниться</a>
        @endif
    `
)