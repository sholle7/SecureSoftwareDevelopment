package com.zuehlke.securesoftwaredevelopment.domain;

public class Comment {
    private Integer id;
    private int carId;
    private Integer userId;
    private String comment;

    public Comment() {
    }

    public Comment(Integer id ,int carId, Integer userId, String comment) {
        this.id = id;
        this.carId = carId;
        this.userId = userId;
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
