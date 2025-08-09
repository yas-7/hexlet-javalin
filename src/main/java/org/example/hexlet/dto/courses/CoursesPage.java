package org.example.hexlet.dto.courses;

import org.example.hexlet.model.Course;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CoursesPage {
    private List<Course> courses;
    private String header;
    private String term;
    private Boolean visited;

    public Boolean isVisited() {
        return visited;
    }
}
