package com.lolisapp.traductorquechua.bean;

/**
 * Created by USUARIO on 02/09/2017.
 */

public class User {

    private Long userId;
    private String email;
    private String lastName;
    private String firstName;
    private String Phone;
    private String CountryId;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {

        return userId;
    }



    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String countryId) {
        CountryId = countryId;
    }
}