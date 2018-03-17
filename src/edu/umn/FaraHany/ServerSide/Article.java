package edu.umn.FaraHany.ServerSide;

import java.util.ArrayList;

public class Article {
    private int id;
    private int indentLevel;
    private int parent;
    private String title;
    private String content;
    private ArrayList<Integer> replies;



    public Article(int id, int indentLevel, int parent, String title, String content) {
        this.id = id;
        this.indentLevel = indentLevel;
        this.parent = parent;
        this.title = title;
        this.content = content;
    }

//    public Article(int id, int parent, String title, String content) {
//        this.id = id;
//        this.parent = parent;
//        this.title = title;
//        this.content = content;
//    }

    public int getIndentLevel() {
        return indentLevel;
    }

    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Integer> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Integer> replies) {
        this.replies = replies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (parent == -1) {
            return  "id: " + id + "\n" +
                    "title: " + title + "\n" +
                    "content: " + content + "\n";
        } else {
            return  "reply to: " + parent + "\n" +
                    "id: " + id + "\n" +
                    "title: " + title + "\n" +
                    "content: " + content + "\n";
        }
    }
}
