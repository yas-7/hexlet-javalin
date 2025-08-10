@import gg.jte.Content
@import org.example.hexlet.dto.BasePage
@param Content content
@param BasePage page = null

<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Hexlet Javalin Example</title>
    </head>
    <body>
    	<header>
    		<ul>
    			<li><a href="/">Главная</a></li>
    			<li><a href="/users">Users</a></li>
    			<li><a href="/courses">Courses</a></li>
    		</ul>
    	</header>

        <main>
            @if(page != null && page.getFlash() != null)
                <i>${page.getFlash()}</i>
            @endif
        	<h1>specific content part</h1>
        	${content}
        </main>
    </body>
</html>
