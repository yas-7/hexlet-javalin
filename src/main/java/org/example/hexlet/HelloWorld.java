package org.example.hexlet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.rendering.template.JavalinJte;
import org.example.hexlet.controller.CoursesController;
import org.example.hexlet.controller.SessionsController;
import org.example.hexlet.controller.UsersController;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.repository.BaseRepository;
import org.example.hexlet.util.NamedRoutes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.javalin.rendering.template.TemplateUtil.model;

public class HelloWorld {

    private static final List<Map<String, String>> COMPANIES = List.of(
            Map.of("phone", "(900) 202-4560", "name", "John & Brothers", "id", "1"),
            Map.of("phone", "(505) 640-3456", "name", "Morar-Wehner", "id", "6"),
            Map.of("phone", "(959) 202-6260", "name", "O'Conner and Sons", "id", "2")
    );

    public static void main(String[] args) throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1;");

        var dataSource = new HikariDataSource(hikariConfig);
        // Получаем путь до файла в src/main/resources
        var url = HelloWorld.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));

        // Получаем соединение, создаем стейтмент и выполняем запрос
        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        // Создаем приложение
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });
        // Описываем, что загрузится по адресу /
        // app.get("/", ctx -> ctx.render("index.jte"));
        // Отображение формы логина "/sessions/build"
        app.get(NamedRoutes.sessionBuildPath(), SessionsController::build);
        // Процесс логина "/sessions"
        app.post(NamedRoutes.sessionsPath(), SessionsController::create);
        // Процесс выхода из аккаунта "/sessions"
        app.delete(NamedRoutes.sessionsPath(), SessionsController::destroy);

        app.get("/", ctx ->{
            var currentUser = ctx.sessionAttribute("currentUser");
            var page = new MainPage((String) currentUser);
            ctx.render("index.jte", model("page", page));
        });

//        app.get("/users", ctx -> ctx.result("GET /users"));
//        app.post("/users", ctx -> ctx.result("POST /users"));
        app.get("/hello", ctx -> {
            var name = ctx.queryParam("name");
            ctx.result("Hello, " + name + "!");
        });

        app.get("/companies/{id}", ctx -> {
            var index = ctx.pathParam("id");
            Map<String, String> company = COMPANIES
                    .stream()
                    .filter(el -> el.get("id").equals(index))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundResponse("Company not found"));

            ctx.json(company);
        });
        app.get("/companies", ctx -> ctx.json(COMPANIES));

        app.get(NamedRoutes.coursesPath(), CoursesController::index);
        app.get(NamedRoutes.buildCoursePath(), CoursesController::build); // должен быть выше show
        app.get(NamedRoutes.coursePath("{id}"), CoursesController::show);
        app.post(NamedRoutes.coursesPath(), CoursesController::create);


        app.get(NamedRoutes.usersPath(), UsersController::index);

        app.get(NamedRoutes.buildUserPath(), UsersController::build);

        app.post(NamedRoutes.usersPath(), UsersController::create);

        app.get(NamedRoutes.userPath("{id}"), UsersController::show);
//            ctx -> {
//            var id = ctx.pathParam("id");
//            ctx.contentType("html");
//            ctx.result("<h1>" + id + "</h1>");
//            open http://localhost:7070/users/%3Cscript%3Ealert('attack!')%3B%3C%2Fscript%3E });

        app.delete(NamedRoutes.userPath("{id}"), UsersController::destroy);

        app.start(7070); // Стартуем веб-сервер
    }
}
