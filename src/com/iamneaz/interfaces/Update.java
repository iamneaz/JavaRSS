package com.iamneaz.interfaces;

import com.iamneaz.rssMaterial.Feed;

public interface Update {

    void runUpdateRSS(String urlAddress, String fileAddress);

    int findIndex(Feed feedURL, Feed feedFile);

    void writeUpdates(Feed feedFile);

    void updater(int sameItemIndex, Feed feedURL, Feed feedFile);
}
