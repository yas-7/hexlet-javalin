package org.example.hexlet.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import org.example.hexlet.model.User;

public class UserRepository {
    @Getter
    private static List<User> entities = new ArrayList<>();

    public static void save(User user) {
        user.setId((long) entities.size() + 1);
        user.setCreatedAt(LocalDateTime.now());
        entities.add(user);
    }

    public static List<User> search(String term) {
        return entities.stream()
                .filter(entity -> entity.getName().toLowerCase().startsWith(term.toLowerCase()))
                .toList();
    }

    public static Optional<User> find(Long id) {
        var maybeUser = entities.stream()
                .filter(entity -> entity.getId() == id)
                .findAny();
        return maybeUser;
    }

    public static void delete(Long id) {
        entities.removeIf(user -> user.getId() == id);
    }

    public static void removeAll() {
        entities = new ArrayList<>();
    }
}