package com.iamneaz;

import java.util.List;

public class RSSHeader {
    private String title ;
    private String link ;
    private String guid ;
    private List<RSSMedia> rssMediaList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List<RSSMedia> getRssMediaList() {
        return rssMediaList;
    }

    public void setRssMediaList(List<RSSMedia> rssMediaList) {
        this.rssMediaList = rssMediaList;
    }
}
