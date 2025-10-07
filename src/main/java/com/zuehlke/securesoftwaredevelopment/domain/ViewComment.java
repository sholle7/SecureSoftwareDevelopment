package com.zuehlke.securesoftwaredevelopment.domain;

public class ViewComment {
    private String personName;
    private String comment;
    private int userId;
    private int commentId;

    public ViewComment(String personName, String comment, int userId, int commentId) {
        this.personName = personName;
        this.comment = comment;
        this.userId = userId;
        this.commentId = commentId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public int getCommentId() { return commentId; }

    public void setCommentId(int commentId) { this.commentId = commentId; }
}