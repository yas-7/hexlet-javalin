package org.example.hexlet.repository;

import org.example.hexlet.model.Course;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository extends BaseRepository {
    public static void save(Course course) {
        String sql = "INSERT INTO courses (name, description) VALUES (?, ?)";
        try (var conn = dataSource.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, course.getDescription());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                course.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Course> find(Long id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        try (var conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Course course = new Course(id, name, description);

                return Optional.of(course);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Course> findByName(String term) {
        String sql = "SELECT * FROM courses WHERE name ILIKE ?";
        try (var conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, "%" + term + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                courses.add(new Course(id, name, description));
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Course> getEntities() {
        String sql = "SELECT * FROM courses";
        try (var conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                courses.add(new Course(id, name, description));
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
