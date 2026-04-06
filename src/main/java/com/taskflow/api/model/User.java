package com.taskflow.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users") // Store this in MongoDB collection called users
@Data // Lombok: auto-generate getters, setters, toString
@NoArgsConstructor // Lombok: auto-generate empty constructor
@AllArgsConstructor // Lombok: auto-generate constructors with all fields
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private UserRole role = UserRole.USER;

    @CreatedDate
    private LocalDateTime createdAt;
}
