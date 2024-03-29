package com.example.corporatemessenger.domen.dto;

import com.example.corporatemessenger.domen.User;

public class MessagePOJO {

    private String text;
    private String tag;
    private User author;
    private String filename;

    public MessagePOJO(String text, String tag, User author) {
        this.text = text;
        this.tag = tag;
        this.author = author;
    }

    public String getAuthorName() {
        return author == null ? "none" : author.getUsername();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "MessagePOJO{"
                + "text='" + text + '\''
                + ", tag='" + tag + '\''
                + ", author=" + author
                + '}';
    }
}
