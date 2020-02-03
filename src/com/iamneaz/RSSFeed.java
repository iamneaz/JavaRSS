package com.iamneaz;

import java.util.Deque;
import java.util.List;

public class RSSFeed {

    private RSSHeader rssHeader ;
    private List<RSSItems> entries;

    public RSSHeader getRssHeader() {
        return rssHeader;
    }

    public void setRssHeader(RSSHeader rssHeader) {
        this.rssHeader = rssHeader;
    }

    public List<RSSItems> getEntries() {
        return entries;
    }

    public void setEntries(List<RSSItems> entries) {
        this.entries = entries;
    }



}
