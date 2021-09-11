package com.example.firstSpringProgect.domen.dto;

import com.example.firstSpringProgect.domen.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class MessagePOJO {
    //@NotBlank(message = "Please fill the message")
    //@Length(max = 2048, message = "Message too long (more than 2kB)")
    private String text;
    private String tag;
    private User author;
    private String filename;




    public String getAuthorName(){
        return author != null ? author.getUsername():"none";
    }

    public String getText() {
        return text;
    }

    public MessagePOJO(String text, String tag, User author) {
        this.text = text;
        this.tag = tag;
        this.author = author;
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
        return "MessagePOJO{" +
                "text='" + text + '\'' +
                ", tag='" + tag + '\'' +
                ", author=" + author +
                '}';
    }
}
