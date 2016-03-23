package com.android.incongress.cd.conference.beans;

/**
 * Created by Jacky on 2015/12/16.
 */
public class FacultyBean {
    private String facultyId;
    private String roleId;
    private String roleName;
    private String facultyName;
    private String facultyEnName;
    private String roleEnName;

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getFacultyEnName() {
        return facultyEnName;
    }

    public void setFacultyEnName(String facultyEnName) {
        this.facultyEnName = facultyEnName;
    }

    public String getRoleEnName() {
        return roleEnName;
    }

    public void setRoleEnName(String roleEnName) {
        this.roleEnName = roleEnName;
    }

    @Override
    public String toString() {
        return "FacultyBean{" +
                "facultyId='" + facultyId + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", facultyName='" + facultyName + '\'' +
                ", facultyEnName='" + facultyEnName + '\'' +
                ", roleEnName='" + roleEnName + '\'' +
                '}';
    }
}
