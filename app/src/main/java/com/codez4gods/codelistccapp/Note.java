package com.codez4gods.codelistccapp;

public class Note {
    //This is a simple note class.
    private static final long serialVersionUID = 1L;
    private int id;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private String image_url;
    private String title;
    private String content;
    private String date;

    public Note(){
        super();
    }

    public Note(String image_url, String title, String content, String date) {
        this.image_url = image_url;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    //sets and gets note's id, title, contenti date and color

    public String getTitle(){
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;

    }
}
