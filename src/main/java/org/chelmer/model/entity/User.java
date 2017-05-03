package org.chelmer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private final String name;
    private final LoxUuid uuid;
    private final boolean isAdmin;
    private final boolean changePassword;

    @JsonCreator
    public User(@JsonProperty("name") String name, @JsonProperty("uuid") LoxUuid uuid, @JsonProperty("isAdmin") boolean isAdmin, @JsonProperty("changePassword") boolean changePassword) {
        this.name = name;
        this.uuid = uuid;
        this.isAdmin = isAdmin;
        this.changePassword = changePassword;
    }

    public String getName() {
        return name;
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isChangePassword() {
        return changePassword;
    }
}
