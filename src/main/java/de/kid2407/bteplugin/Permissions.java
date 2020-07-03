package de.kid2407.bteplugin;

/**
 * User: Tobias Franz
 * Date: 02.07.2020
 * Time: 19:16
 */
public enum Permissions {

    FLY_USE("fly"),
    SPEED_USE("speed"),
    VISIT_CREATE("visit.create");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return "bte." + permission;
    }
}
