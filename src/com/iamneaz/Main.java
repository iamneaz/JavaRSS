package com.iamneaz;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        FileOutputStream writer = new FileOutputStream("a.rss");
        FileOutputStream writer2 = new FileOutputStream("b.rss");
        writer.close();
        writer2.close();


        RSSReaderImpl rssReaderImpl = new RSSReaderImpl();

        RSSFeed rssFeed;
        rssFeed = rssReaderImpl.runRSSReader("http://rss.cnn.com/rss/edition.rss",true);
        RSSWriterImpl rssWriterImpl = new RSSWriterImpl();
        rssWriterImpl.runRSSWriter(rssFeed,"","a.rss");
        rssFeed =rssReaderImpl.runRSSReader("a.rss",false);
        System.out.println(rssFeed.getEntries().get(0).getRssMediaList().get(0).getUrl());
        rssWriterImpl.runRSSWriter(rssFeed,"","b.rss");

    }
}
