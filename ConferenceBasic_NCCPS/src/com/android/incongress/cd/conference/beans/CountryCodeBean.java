package com.android.incongress.cd.conference.beans;

/**
 * Created by Jacky on 2016/2/28.
 */
public class CountryCodeBean {
    private String country;
    private String code;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CountryCodeBean{" +
                "country='" + country + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
