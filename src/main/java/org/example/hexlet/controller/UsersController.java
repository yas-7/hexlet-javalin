package org.example.hexlet.controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import org.example.hexlet.util.NamedRoutes;
import org.example.hexlet.dto.users.BuildUserPage;
import org.example.hexlet.dto.users.UserPage;
import org.example.hexlet.dto.users.UsersPage;
import org.example.hexlet.model.User;
import org.example.hexlet.repository.UserRepository;

import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UsersController {
    public static void index(Context ctx) {
        String term = ctx.queryParam("term");
        List<User> users;
        if (term != null) {
            users = UserRepository.search(term);
        } else {
            users = UserRepository.getEntities();
        }
        String flash = ctx.consumeSessionAttribute("flash");

        UsersPage page = new UsersPage(users, "User's page header", term, flash);
        ctx.render("users/index.jte", model("page", page));
    }

    public static void show(Context ctx) {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var user = UserRepository
                .find(id)
                .orElseThrow(() -> new NotFoundResponse("USER " + id + " not found"));
        var page = new UserPage(user);
        ctx.render("users/show.jte", model("page", page));
    }

    public static void build(Context ctx) {
        var page = new BuildUserPage();
        ctx.render("users/build.jte", model("page", page));
    }

    public static void create(Context ctx) {
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
            ctx.sessionAttribute("flash", "USER HAS BEEN CREATED!");
            ctx.redirect(NamedRoutes.usersPath());
        } catch (ValidationException e) {
            var page = new BuildUserPage(name, email, e.getErrors());
            System.out.println(e.getErrors());
            ctx.render("users/build.jte", model("page", page));
        }
    }

    public static void destroy(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        UserRepository.delete(id);
        ctx.redirect(NamedRoutes.usersPath());
    }

    public static void edit(Context ctx) {}
    public static void update(Context ctx) {}

}
