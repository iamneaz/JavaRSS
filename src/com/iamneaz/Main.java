package com.iamneaz;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        RSSReaderImpl rssReaderImpl = new RSSReaderImpl();
        //System.out.println();
        rssReaderImpl.runRSSReader("http://rss.cnn.com/rss/edition.rss");

    }
}
