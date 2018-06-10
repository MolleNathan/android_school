package com.example.nathan.schoolmollenathan;

import java.util.List;

/**
 * Created by Nathan on 10/06/2018.
 */

public class JsonResponseSchool {
    private List<School> schools;
    private School school;
    private String error;

    public JsonResponseSchool(List<School> schools) {
        this.schools = schools;
    }

    public JsonResponseSchool(School school) {
        this.school = school;
    }

    public JsonResponseSchool(String error) {
        this.error = error;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
