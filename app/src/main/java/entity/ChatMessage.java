package entity;

import java.util.Date;

public class ChatMessage {
    private String name;
    private String message;
    private Type type;
    private Date date;

    public ChatMessage() {

    }

    public ChatMessage(String message, Type type, Date date) {
        super();
        this.message = message;
        this.type = type;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getData() {
        return date;
    }

    public void setData(Date data) {
        this.date = data;
    }

    public enum Type {
        INCOUNT, OUTCOUNT
    }

}