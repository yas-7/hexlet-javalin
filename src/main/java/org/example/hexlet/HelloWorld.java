package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.validation.ValidationException;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.dto.users.BuildUserPage;
import org.example.hexlet.dto.users.UserPage;
import org.example.hexlet.dto.users.UsersPage;
import org.example.hexlet.model.Course;
import org.example.hexlet.model.User;
import org.example.hexlet.repository.UserRepository;

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

            CoursesPage page = new CoursesPage(courses, header, term);
            ctx.render("courses/index.jte", model("page", page));
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

        app.get("/users", ctx -> {
            String term = ctx.queryParam("term");
            List<User> users;
            if (term != null) {
                users = UserRepository.search(term);
            } else {
                users = UserRepository.getEntities();
            }

            UsersPage page = new UsersPage(users, "User's page header", term);
            ctx.render("users/index.jte", model("page", page));
        });

        app.get("/users/build", ctx -> {
            var page = new BuildUserPage();
            ctx.render("users/build.jte", model("page", page));
        });

        app.post("/users", ctx -> {
            var name = ctx.formParam("name").trim();
            var email = ctx.formParam("email").trim().toLowerCase();

            try {
                var passwordConfirmation = ctx.formParam("passwordConfirmation");
                var password = ctx.formParamAsClass("password", String.class)
                        .check(value -> value.equals(passwordConfirmation), "Пароли не совпадают")
                        .check(value -> value.length() > 3, "Длина пароля должна быть не менее 3х символов")
                        .get();
                var user = new User(name, email, password);
                UserRepository.save(user);
                ctx.redirect("/users");
            } catch (ValidationException e) {
                var page = new BuildUserPage(name, email, e.getErrors());
                System.out.println(e.getErrors());
                ctx.render("users/build.jte", model("page", page));
            }
        });

        app.get("/users/{id}", ctx -> {
//            var id = ctx.pathParam("id");
//            ctx.contentType("html");
//            ctx.result("<h1>" + id + "</h1>");
////            open http://localhost:7070/users/%3Cscript%3Ealert('attack!')%3B%3C%2Fscript%3E

            long id = ctx.pathParamAsClass("id", Long.class).get();
            var user = UserRepository
                        .find(id)
                        .orElseThrow(() -> new NotFoundResponse("USER " + id + " not found"));
            var page = new UserPage(user);
            ctx.render("users/show.jte", model("page", page));
        });

        app.start(7070); // Стартуем веб-сервер
    }
}
