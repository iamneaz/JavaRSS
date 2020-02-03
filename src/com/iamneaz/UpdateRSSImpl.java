package com.iamneaz;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateRSSImpl implements UpdateRSS {


    @Override
    public void runUpdateRSS() {
        RSSReaderImpl rssReaderImpl = new RSSReaderImpl();
        RSSFeed rssFeedURL;
        RSSFeed rssFeedFile;
        int sameItemIndex;
        try {
            rssFeedURL = rssReaderImpl.runRSSReader("http://rss.cnn.com/rss/edition.rss", true);
            rssFeedFile = rssReaderImpl.runRSSReader("a.rss", false);

            // checking which item is a match for the first item of  a.rss
            sameItemIndex = findIndex(rssFeedURL, rssFeedFile);
            //updating a.rss
            updater(sameItemIndex, rssFeedURL, rssFeedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int findIndex(RSSFeed rssFeedURL, RSSFeed rssFeedFile) {
        int sameItemIndex = 0;
        for (int i = 0; i < rssFeedURL.getEntries().size() - 1; i++) {
            if (rssFeedURL.getEntries().get(i).getTitle().replace("<![CDATA[", "").replace("]]>", "").trim().equalsIgnoreCase(rssFeedFile.getEntries().get(0).getTitle().replace("<![CDATA[", "").replace("]]>", "").trim())) {
                sameItemIndex = i;
                break;
            } else {
                sameItemIndex = -1;
            }

        }
        return sameItemIndex;
    }

    public void writeUpdates(RSSFeed rssFeedFile) {
        RSSWriterImpl rssWriter = new RSSWriterImpl();
        try {
            FileOutputStream writer = new FileOutputStream("a.rss");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rssWriter.runRSSWriter(rssFeedFile, "", "a.rss", false);
    }

    public void updater(int sameItemIndex, RSSFeed rssFeedURL, RSSFeed rssFeedFile) {
        if (sameItemIndex == -1) {
            rssFeedFile.setRssHeader(rssFeedURL.getRssHeader());
            rssFeedFile.setEntries(rssFeedURL.getEntries());
        } else if (sameItemIndex > 0) {
            for (int i = sameItemIndex; i >= 0; i--) {
                RSSItems rssItem = rssFeedURL.getEntries().get(i);
                rssFeedFile.getEntries().add(0, rssItem);
            }
            rssFeedFile.setRssHeader(rssFeedURL.getRssHeader());
        } else {
            System.out.println("no changes");
        }
        writeUpdates(rssFeedFile);
    }


}
