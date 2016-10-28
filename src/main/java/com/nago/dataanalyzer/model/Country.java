package com.nago.dataanalyzer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Country {
    @Id
    private String code;
    private String name;
    private Float internetUsers;
    private Float adultLiteracyRate;

    public Country(){}

    public Country(CountryBuilder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Float internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Float getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(Float adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers=" + internetUsers +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }

    public static class CountryBuilder{
        private String code;
        private String name;
        private Float internetUsers;
        private Float adultLiteracyRate;

        public CountryBuilder(String code, String name){
            this.code = code;
            this.name = name;
        }

        public CountryBuilder withInternetUsers(Float internetUsers){
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(Float adultLiteracyRate){
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build(){return new Country(this);}
    }
}
