package org.example.hexlet.controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import org.example.hexlet.dto.courses.BuildCoursePage;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.model.Course;
import org.example.hexlet.repository.CourseRepository;
import org.example.hexlet.util.NamedRoutes;

import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class CoursesController {

    public static void show(Context ctx) {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var course = CourseRepository
                .find(id)
                .orElseThrow(() -> new NotFoundResponse("Course " + id + " not found"));
        var page = new CoursePage(course);
        ctx.render("courses/show.jte", model("page", page));
    }

    public static void index(Context ctx) {
        String header = "Курсы по программированию";
        String term = ctx.queryParam("term");
        List<Course> courses;
        if (term != null) {
            courses = CourseRepository.findByName(term);
        } else {
            courses = CourseRepository.getEntities();
        }

        boolean visited = Boolean.parseBoolean(ctx.cookie("visited"));
        String flash = ctx.consumeSessionAttribute("flash");

        CoursesPage page = new CoursesPage(courses, header, term, visited);
        page.setFlash(flash);
        ctx.render("courses/index.jte", model("page", page));

        ctx.cookie("visited", String.valueOf(true));
    }

    public static void build(Context ctx) {
        var page = new BuildCoursePage();
        ctx.render("courses/build.jte", model("page", page));
    }

    public static void create(Context ctx) {
        var description = ctx.formParam("description").trim().toLowerCase();
        try {
            var name = ctx.formParamAsClass("name", String.class)
                    .check(value -> value.trim().length() >= 2, "Длина названия курса должна быть не менее 2х символов")
                    .get();

            var course = new Course(name, description);
            CourseRepository.save(course);

            ctx.sessionAttribute("flash", "COURSE HAS BEEN CREATED!");
            ctx.redirect(NamedRoutes.coursesPath());
        } catch (ValidationException e) {
            var page = new BuildCoursePage("", description, e.getErrors());

            ctx.render("courses/build.jte", model("page", page));
        }
    }

}
