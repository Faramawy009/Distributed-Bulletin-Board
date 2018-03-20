package edu.umn.FaraHany.ServerSide;

import java.util.ArrayList;

public class BulletinBoard {
    private static ArrayList<Article> articles;
    static {
        articles = new ArrayList<>();
    }

    public static ArrayList<Article> getArticles() {
        return articles;
    }
    public static String post(String title, String content) {
        int id = articles.size()+1;
        Article a = new Article(id,0, -1, title, content);
        articles.add(a);
        return "Article posted with id: " + id;
    }

    public static String postWithId(int id, String title, String content) {
        Article a = new Article(id,0, -1, title, content);
        articles.add(id-1,a);
        return "Article posted with id: " + id;
    }

    public static void insert(String el) {
        if(el == null || el.equals(""))
            return;
        Article newArt = null;
        String[] elements = el.split(";");
        if(elements.length==6) {
            newArt = new Article(Integer.parseInt(elements[0]),
                    Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
                    elements[3], elements[4], elements[5]); }
        else {
            newArt = new Article(Integer.parseInt(elements[0]),
                    Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
                    elements[3], elements[4]);
        }
        articles.add(newArt);
    }

    public static void clear(){
        articles.clear();
    }

    public static String reply(int parentId, String title, String content) {
        int id = articles.size()+1;
        if (!isElement(parentId)){
            System.out.println(BulletinBoard.class.getName() + ": invalid reply id input");
            return(BulletinBoard.class.getName() + ": invalid reply id input");
        }
        int indent = articles.get(parentId-1).getIndentLevel()+1;
        Article a = new Article(id,indent, parentId, title, content);
        articles.add(a);
        return "Reply posted with id: "+id;
    }

    public static String replyWithId(int id, int parentId, String title, String content) {
        if (!isElement(parentId)){
            System.out.println(BulletinBoard.class.getName() + ": invalid reply id input");
            return(BulletinBoard.class.getName() + ": invalid reply id input");
        }
        int indent = articles.get(parentId-1).getIndentLevel()+1;
        Article a = new Article(id,indent, parentId, title, content);
        articles.add(id-1,a);
        return "Reply posted with id: "+id;
    }


    public static String read() {
        String listInfo = "";
        for(Article a: articles) {
            listInfo += (repeat("  ",a.getIndentLevel()) +a.getId() +" "+ a.getTitle() + " ");
            if(a.getParent()!=-1) {
                listInfo += "reply to article " + a.getParent();
            }
            listInfo+= "\n";
        }
        return listInfo;
    }

    public static String choose(int id) {
        if (!isElement(id)){
            System.out.println(BulletinBoard.class.getName() + ": invalid id input");
            return (BulletinBoard.class.getName() + ": invalid id input");
        }
        return ""+articles.get(id-1);
    }

    public static int getSize(){ return  articles.size();}

    private static String repeat(String s, int count) {
        return count > 0 ? s + repeat(s, --count) : "";
    }
    public static boolean isElement(int id) {
        return id <= articles.size();
    }

    public static String buildDB() {
        StringBuilder DB = new StringBuilder();
        DB.append(getSize()+"()");
        for(int i=0; i<BulletinBoard.getSize(); i++) {
            Article a = BulletinBoard.getArticles().get(i);
            DB.append(a.getId()+";");
            DB.append(a.getIndentLevel()+";");
            DB.append(a.getParent()+";");
            DB.append(a.getTitle()+";");
            DB.append(a.getContent()+";");
            int k=0;
            for(k=0; k<a.getReplies().size(); k++) {
                DB.append(a.getReplies().get(k)+",");
            }
            DB.append("#");
        }
        return DB.toString();
    }

    public static String builDeltadDB(int startIndex) {
        StringBuilder DB = new StringBuilder();
        for(int i=startIndex; i<BulletinBoard.getSize(); i++) {
            Article a = BulletinBoard.getArticles().get(i);
            DB.append(a.getId()+";");
            DB.append(a.getIndentLevel()+";");
            DB.append(a.getParent()+";");
            DB.append(a.getTitle()+";");
            DB.append(a.getContent()+";");
            int k=0;
            for(k=0; k<a.getReplies().size(); k++) {
                DB.append(a.getReplies().get(k)+",");
            }
            DB.append("#");
        }
        return DB.toString();
    }

    public static void updateDB(String DB) {
        if(DB == null || DB.equals(""))
            return;
        int remoteVersion = Integer.parseInt(DB.split("\\(\\)")[0]);
        if(remoteVersion<= getSize())
            return;
//        clear();

        String articles = DB.split("\\(\\)")[1];
        String [] separatedArticles = articles.split("#");
        int size = getSize();
        for(int i =size; i<separatedArticles.length; i++) {
            insert(separatedArticles[i]);
        }
    }

    public static void deltaUpdateDB(String delta) {
        if(delta == null || delta.equals(""))
            return;
        String [] separatedArticles = delta.split("#");
        int size = getSize();
        for(int i =0; i<separatedArticles.length; i++) {
            insert(separatedArticles[i]);
        }
    }
}
