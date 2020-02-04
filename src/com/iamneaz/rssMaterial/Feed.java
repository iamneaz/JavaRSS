package com.iamneaz.rssMaterial;

import java.util.List;

public class Feed {

    private Header header;
    private List<Items> entries;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Items> getEntries() {
        return entries;
    }

    public void setEntries(List<Items> entries) {
        this.entries = entries;
    }


}
