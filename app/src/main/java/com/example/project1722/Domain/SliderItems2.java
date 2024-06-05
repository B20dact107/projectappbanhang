package com.example.project1722.Domain;

public class SliderItems2 {
    private String url;
    private String discountCode;

    public SliderItems2() {
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getUrl() {
        return url;
    }




    public SliderItems2(String url, String discountCode) {
        this.url = url;
        this.discountCode = discountCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
