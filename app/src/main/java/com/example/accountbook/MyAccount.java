package com.example.accountbook;

import org.litepal.crud.LitePalSupport;

public class MyAccount extends LitePalSupport {
    private int priceType; //类型，1为支出，0为收入
    private String price;
    private String type; //比如food,fruits等
    private String photoPath=""; //自己拍的照片就需要路径来显示
    private String time;

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
