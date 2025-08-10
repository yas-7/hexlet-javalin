package org.example.hexlet.controller;

import io.javalin.http.Context;
import io.javalin.validation.ValidationException;

public class SessionsController {
    public static void build(Context ctx) {
        ctx.render("sessions/build.jte");
    }

    public static void create(Context ctx) {
        var nickname = ctx.formParam("nickname");
        String password = "";
        try {
            password = ctx.formParamAsClass("password", String.class)
                    .check(value -> value.length() > 3, "Длина пароля должна быть не менее 3х")
                    .get();
        } catch (ValidationException e) {
//            var page = new BuildSessionPage(name, email, e.getErrors());
            System.out.println(e.getErrors());
//            ctx.render("sessions/build.jte", model("page", page));
            ctx.render("sessions/build.jte");
        }

        ctx.sessionAttribute("currentUser", nickname);
        ctx.redirect("/");
    }

    public static void destroy(Context ctx) {
        ctx.sessionAttribute("currentUser", null);
        ctx.redirect("/");
    }
}
