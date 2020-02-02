package com.iamneaz;

public class Main {

    public static void main(String[] args) {
	// write your code here
        RSSReader rssReader = new RSSReader();
        //System.out.println();
        rssReader.runRSSReader("http://rss.cnn.com/rss/edition.rss");

    }
}
