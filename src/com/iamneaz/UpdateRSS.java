package com.iamneaz;

public interface UpdateRSS {

    void runUpdateRSS();
    int findIndex(RSSFeed rssFeedURL,RSSFeed rssFeedFile);
    void writeUpdates(RSSFeed rssFeedFile);
    void updater(int sameItemIndex,RSSFeed rssFeedURL,RSSFeed rssFeedFile);
}
