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
}
