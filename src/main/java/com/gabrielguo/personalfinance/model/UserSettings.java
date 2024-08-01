package com.gabrielguo.personalfinance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usersettings")
@Data
@AllArgsConstructor
public class UserSettings {
    @Id
    private String id;
    private String userId;
    private boolean emailNotifications;
    private String theme;

    public UserSettings() {
        this.emailNotifications = false;
        this.theme = "light";
    }



}
