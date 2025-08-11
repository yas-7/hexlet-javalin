package org.example.hexlet.util;

public class NamedRoutes {
    // Маршрут пользователей
    public static String usersPath() {
        return "/users";
    }

    public static String buildUserPath() {
        return "/users/build";
    }
// Это нужно, чтобы не преобразовывать типы снаружи
    public static String userPath(Long id) {
        return userPath(String.valueOf(id));
    }
    public static String userPath(String id) {
        return "/users/" + id;
    }

    public static String sessionsPath() {
        return "/sessions";
    }

    public static String sessionBuildPath() {
        return "/sessions/build";
    }

    public static String coursesPath() {
        return "/courses";
    }

    public static String buildCoursePath() {
        return "/courses/build";
    }

    public static String coursePath(Long id) {
        return coursePath(String.valueOf(id));
    }
    public static String coursePath(String id) {
        return coursesPath() + "/" + id;
    }
}
