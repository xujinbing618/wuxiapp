package com.magus.enviroment.ep.bean;

/**
 * 城市实体类
 * Created by pau on 15/5/26.
 */
public class CityInfoBean {
    private String city;
    private String province;


    private String cityPinYin;

    public CityInfoBean() {
    }

    public CityInfoBean(String city, String province) {
        this.city = city;
        this.province = province;
    }

    public String getCityPinYin() {
        return cityPinYin;
    }

    public void setCityPinYin(String cityPinYin) {
        this.cityPinYin = cityPinYin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
