package com.android.incongress.cd.conference.beans;

/**
 * Created by Jacky on 2015/12/16.
 */
public class RoleBean {
    private int roleId;
    private String name;
    private String enName;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    @Override
    public String toString() {
        return "RoleBean{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                ", enName='" + enName + '\'' +
                '}';
    }
}
