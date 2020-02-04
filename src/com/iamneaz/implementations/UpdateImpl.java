package com.iamneaz.implementations;

import com.iamneaz.interfaces.Update;
import com.iamneaz.rssMaterial.Feed;
import com.iamneaz.rssMaterial.Items;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateImpl implements Update {

    @Override
    public void runUpdateRSS(String urlAddress, String fileAddress) {

        try {
            ReadImpl rssReaderImpl = new ReadImpl();
            Feed feedURL;
            Feed feedFile;
            int sameItemIndex;
            feedURL = rssReaderImpl.runRSSReader(urlAddress, true);
            feedFile = rssReaderImpl.runRSSReader(fileAddress, false);

            // checking which item is a match for the first item of  a.rss
            sameItemIndex = findIndex(feedURL, feedFile);
            //updating a.rss
            updater(sameItemIndex, feedURL, feedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int findIndex(Feed feedURL, Feed feedFile) {
        int sameItemIndex = 0;
        /*
            The for loop checks the title of each item of the rss feed from url is the same as the 1st title of the 1st item
            of the rss feed of the file and stores the index in sameItemIndex;
            if none matches, then the variable sameItemIndex is -1
         */
        for (int i = 0; i < feedURL.getEntries().size() - 1; i++) {
            if (feedURL.getEntries().get(i).getTitle().replace("<![CDATA[", "").replace("]]>", "").trim().equalsIgnoreCase(feedFile.getEntries().get(0).getTitle().replace("<![CDATA[", "").replace("]]>", "").trim())) {
                sameItemIndex = i;
                break;
            } else {
                sameItemIndex = -1;
            }

        }
        return sameItemIndex;
    }

    public void writeUpdates(Feed feedFile) {
        WriteImpl rssWriter = new WriteImpl();
        try {
            FileOutputStream writer = new FileOutputStream("a.rss");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rssWriter.runRSSWriter(feedFile, "", "a.rss", false);
    }

    public void updater(int sameItemIndex, Feed feedURL, Feed feedFile) {
        /*
            1st condition checks if none of the items match
            2nd condition checks if a match in found in an index
            3rd condition checks if the file is already updated
         */
        if (sameItemIndex == -1) {
            feedFile.setHeader(feedURL.getHeader());
            feedFile.setEntries(feedURL.getEntries());
        } else if (sameItemIndex > 0) {
            for (int i = sameItemIndex; i >= 0; i--) {
                Items rssItem = feedURL.getEntries().get(i);
                feedFile.getEntries().add(0, rssItem);
            }
            feedFile.setHeader(feedURL.getHeader());
        } else {
            System.out.println("no changes");
        }
        writeUpdates(feedFile);
    }


}
