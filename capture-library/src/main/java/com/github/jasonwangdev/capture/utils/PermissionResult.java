package com.github.jasonwangdev.capture.utils;

/**
 * Created by Jason on 2017/7/2.
 */

public class PermissionResult {

    private String permission;
    private boolean grant;
    private boolean choseNeverAskAgain;


    public PermissionResult(String permission, boolean grant, boolean choseNeverAskAgain) {
        this.permission = permission;
        this.grant = grant;
        this.choseNeverAskAgain = choseNeverAskAgain;
    }


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isGrant() {
        return grant;
    }

    public void setGrant(boolean grant) {
        this.grant = grant;
    }

    public boolean isChoseNeverAskAgain() {
        return choseNeverAskAgain;
    }

    public void setChoseNeverAskAgain(boolean choseNeverAskAgain) {
        this.choseNeverAskAgain = choseNeverAskAgain;
    }


    @Override
    public String toString() {
        return "PermissionResult{" +
                "permission='" + permission + '\'' +
                ", grant=" + grant +
                ", choseNeverAskAgain=" + choseNeverAskAgain +
                '}';
    }

}
