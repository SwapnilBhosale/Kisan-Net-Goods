package com.kng.app.kngapp;

/**
 * Created by hp on 19/12/2016.
 */

public class Categoris {

    private long category_id;
    private String category_name;
    private  String category_image_url;

    @Override
    public String toString() {
        return "Categoris{" +
                "category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                ", category_image_url='" + category_image_url + '\'' +
                '}';
    }


    public long getCategory_id() {

        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image_url() {
        return category_image_url;
    }

    public void setCategory_image_url(String category_image_url) {
        this.category_image_url = category_image_url;
    }
}
