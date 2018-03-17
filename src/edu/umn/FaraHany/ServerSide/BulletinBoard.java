package edu.umn.FaraHany.ServerSide;

import java.util.ArrayList;

public class BulletinBoard {
    private static ArrayList<Article> articles;
    static {
        articles = new ArrayList<>();
    }

    public static String post(String title, String content) {
//        String [] elements = msg.split(";");
//        if(elements.length!=2) {
//            return -1;
//        }
        int id = articles.size()+1;
        Article a = new Article(id,0, -1, title, content);
        articles.add(a);
        return "Article posted with id: " + id;
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
            System.out.println(BulletinBoard.class.getName() + ": invalid reply id input");
            return (BulletinBoard.class.getName() + ": invalid reply id input");
        }
        return ""+articles.get(id-1);
    }

    public int getSize(){ return  articles.size();}

    private static String repeat(String s, int count) {
        return count > 0 ? s + repeat(s, --count) : "";
    }
    public static boolean isElement(int id) {
        return id <= articles.size();
    }
}
