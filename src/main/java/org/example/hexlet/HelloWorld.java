package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.rendering.template.JavalinJte;
import org.example.hexlet.controller.UsersController;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.model.Course;

import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class HelloWorld {
    private static final List<Course> COURSES = List.of(
            new Course(1L, "oop", "some cool course about oop"),
            new Course(2L, "lambda", "some cool course about lambda"),
            new Course(2L, "oop principles", "The core principles of OOP are encapsulation, inheritance, polymorphism, and abstraction")
    );
    private static final List<Map<String, String>> COMPANIES = List.of(
            Map.of("phone", "(900) 202-4560", "name", "John & Brothers", "id", "1"),
            Map.of("phone", "(505) 640-3456", "name", "Morar-Wehner", "id", "6"),
            Map.of("phone", "(959) 202-6260", "name", "O'Conner and Sons", "id", "2")
    );

    public static void main(String[] args) {
        // Создаем приложение
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });
        // Описываем, что загрузится по адресу /
        app.get("/", ctx -> ctx.render("index.jte"));

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

        app.get("/courses", ctx -> {
            String header = "Курсы по программированию";
            String term = ctx.queryParam("term");
            List<Course> courses;
            if (term != null) {
                courses = COURSES.stream()
                        .filter(c -> c.getName().toLowerCase().contains(term.toLowerCase()) || c.getDescription().contains(term.toLowerCase()))
                        .toList();
            } else {
                courses = COURSES;
            }

            boolean visited = Boolean.parseBoolean(ctx.cookie("visited"));
            CoursesPage page = new CoursesPage(courses, header, term, visited);
            ctx.render("courses/index.jte", model("page", page));
            ctx.cookie("visited", String.valueOf(true));
        });

        app.get("/courses/{id}", ctx -> {
            long id = ctx.pathParamAsClass("id", Long.class).get();
            var course = COURSES.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundResponse("Course " + id + " not found"));
            var page = new CoursePage(course);
            ctx.render("courses/show.jte", model("page", page));
        });

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
