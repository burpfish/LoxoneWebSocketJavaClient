package org.chelmer.model.entity;

public class User {
    private final String name;
    private final LoxUuid uuid;
    private final boolean isAdmin;
    private final boolean changePassword;

    public User(String name, LoxUuid uuid, boolean isAdmin, boolean changePassword) {
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
