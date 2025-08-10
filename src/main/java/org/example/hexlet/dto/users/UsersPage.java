package org.example.hexlet.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.hexlet.dto.BasePage;
import org.example.hexlet.model.User;

import java.util.List;

@AllArgsConstructor
@Getter
public class UsersPage extends BasePage {
    private List<User> users;
    private String header;
    private String term;
}
