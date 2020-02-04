package com.iamneaz.implementations;

import com.iamneaz.interfaces.Write;
import com.iamneaz.rssMaterial.Feed;
import com.iamneaz.rssMaterial.Items;
import com.iamneaz.rssMaterial.Media;

import java.io.FileWriter;
import java.io.IOException;

public class WriteImpl implements Write {
    /*
        Location is used so the destination of the file can be changed
     */
    @Override
    public void runRSSWriter(Feed feed, String location, String filename, boolean append) {
        try {
            FileWriter writer = new FileWriter(location + filename, append);

            /*
                Header
             */

            writer.write("<?xml version=" + "1.0" + " encoding=" + "UTF-8" + "?>" + "\n");
            String[] xml = feed.getHeader().getXml();
            writer.write("<?xml" + xml[0] + "?>" + "<?xml" + xml[1] + "?>" + "<rss " + feed.getHeader().getRss() + ">" + "<channel>");
            writer.write("<title>" + feed.getHeader().getTitle() + "</title>");
            writer.write("<description>" + feed.getHeader().getDescription() + "</description>");
            writer.write("<link>" + feed.getHeader().getLink() + "</link>");
            //image
            writer.write("<image>" + "<url>" + feed.getHeader().getImageURL() + "</url>");
            writer.write("<title>" + feed.getHeader().getImageTitle() + "</title>");
            writer.write("<link>" + feed.getHeader().getImageLink() + "</link></image>");
            writer.write("<generator>" + feed.getHeader().getGenerator() + "</generator>");
            writer.write("<lastBuildDate>" + feed.getHeader().getLastBuildDate() + "</lastBuildDate>");
            writer.write("<pubDate>" + feed.getHeader().getPubDate() + "</pubDate>");
            writer.write("<copyright>" + feed.getHeader().getCopyright() + "</copyright>");
            writer.write("<language>" + feed.getHeader().getLanguage() + "</language>");
            writer.write("<ttl>" + feed.getHeader().getTtl() + "</ttl>");
            String[] atom = feed.getHeader().getAtom10();
            writer.write("<atom10:link" + atom[0] + "/>");
            writer.write("<feedburner:info " + feed.getHeader().getFeedBurner() + "/>");
            writer.write("<atom10:link" + atom[1] + " />");

            /*
                Items
             */

            for (Items eachItem : feed.getEntries()) {
                writer.write("<item>" + "<title>" + eachItem.getTitle() + "</title>");
                writer.write("<link>" + eachItem.getLink() + "</link>");
                writer.write("<guid" + eachItem.getGuid() + "</guid>");
                writer.write("<media:group>");

                for (Media eachMedia : eachItem.getMediaList()) {
                    writer.write("<media:content ");
                    writer.write("medium=" + eachMedia.getMedium());
                    writer.write("url=" + eachMedia.getUrl());
                    writer.write(" height=" + eachMedia.getHeight());
                    writer.write(" width=" + eachMedia.getWidth());
                    writer.write(" type= \"image/jpeg\" />");
                }
                writer.write("</media:group></item>");


            }
            writer.write("</channel></rss>");
            writer.write("\r\n");   // write new line
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
