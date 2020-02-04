package com.iamneaz.interfaces;

import com.iamneaz.rssMaterial.Feed;

public interface Write {

    void runRSSWriter(Feed feed, String location, String filename, boolean append);
}
