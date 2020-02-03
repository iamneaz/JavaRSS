package com.iamneaz;

import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateRSSImpl implements UpdateRSS{


    @Override
    public void runUpdateRSS() {
        RSSReaderImpl rssReaderImpl = new RSSReaderImpl();
        RSSFeed rssFeedURL;
        RSSFeed rssFeedFile;
        int sameItemIndex=0;
        try {
            rssFeedURL = rssReaderImpl.runRSSReader("http://rss.cnn.com/rss/edition.rss",true);
            rssFeedFile =rssReaderImpl.runRSSReader("a.rss",false);

            // checking which item is a match for the first item of  a.rss
            for(int i=0;i<rssFeedURL.getEntries().size()-1;i++)
            {
                if(rssFeedURL.getEntries().get(i).getTitle().equalsIgnoreCase(rssFeedFile.getEntries().get(0).getTitle()))
                {
                    sameItemIndex=i;
                }

            }
            System.out.println(sameItemIndex);
            if(sameItemIndex>0)
            {
                for(int i=sameItemIndex-1;i>=0;i--)
                {
                    RSSItems rssItem = rssFeedURL.getEntries().get(i);
                    rssFeedFile.getEntries().add(0,rssItem);

                }
            }
            rssFeedFile.setRssHeader(rssFeedURL.getRssHeader());
            RSSWriterImpl rssWriter = new RSSWriterImpl();
            FileOutputStream writer = new FileOutputStream("a.rss");
            rssWriter.runRSSWriter(rssFeedFile,"","a.rss");

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
