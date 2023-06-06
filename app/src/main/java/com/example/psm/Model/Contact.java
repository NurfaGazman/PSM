package com.example.psm.Model;

public class Contact {

    private int user_Id;
    private String message;
    private String contact_no;
    private String name;

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name =name;
    }

    public Contact(){
        user_Id = -1;
        message = "";
        contact_no = "";
        name = "";
    }

    public Contact(String name,String contactno){
        this.name = name;
        this.contact_no = contactno;
        user_Id = -1;
        message = "";
    }
}
