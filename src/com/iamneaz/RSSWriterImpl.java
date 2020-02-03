package com.iamneaz;

import java.io.FileWriter;
import java.io.IOException;

public class RSSWriterImpl implements RSSWriter{


    @Override
    public void runRSSWriter(RSSFeed rssFeed,String location,String filename) {
        try {
            FileWriter writer = new FileWriter(location+filename, true);
            /*
                Header
             */

            writer.write("<?xml version="+"1.0"+" encoding="+"UTF-8"+"?>"+"\n");
            String[] xml = rssFeed.getRssHeader().getXml();
            writer.write("<?xml"+xml[0]+"?>"+"<?xml"+xml[1]+"?>"+"<rss "+rssFeed.getRssHeader().getRss()+">"+"<channel>");
            writer.write("<title>"+rssFeed.getRssHeader().getTitle()+"</title>");
            writer.write("<description>"+rssFeed.getRssHeader().getDescription()+"</description>");
            writer.write("<link>"+rssFeed.getRssHeader().getLink()+"</link>");
            //image
            writer.write("<image>"+"<url>"+rssFeed.getRssHeader().getImageURL()+"</url>");
            writer.write("<title>"+rssFeed.getRssHeader().getImageTitle()+"</title>");
            writer.write("<link>"+rssFeed.getRssHeader().getImageLink()+"</link></image>");
            writer.write("<generator>"+rssFeed.getRssHeader().getGenerator()+"</generator>");
            writer.write("<lastBuildDate>"+rssFeed.getRssHeader().getLastBuildDate()+"</lastBuildDate>");
            writer.write("<pubDate>"+rssFeed.getRssHeader().getPubDate()+"</pubDate>");
            writer.write("<copyright>"+rssFeed.getRssHeader().getCopyright()+"</copyright>");
            writer.write("<language>"+rssFeed.getRssHeader().getLanguage()+"</language>");
            writer.write("<ttl>"+rssFeed.getRssHeader().getTtl()+"</ttl>");
            String[] atom = rssFeed.getRssHeader().getAtom10();
            writer.write("<atom10:link"+atom[0]+"/>");
            writer.write("<feedburner:info "+rssFeed.getRssHeader().getFeedBurner()+"/>");
            writer.write("<atom10:link"+atom[1]+" />");

            /*
                Items
             */
            for(RSSItems eachItem : rssFeed.getEntries())
            {
                writer.write("<item>"+"<title>"+eachItem.getTitle()+"</title>");
                writer.write("<link>"+eachItem.getLink()+"</link>");
                writer.write("<guid"+eachItem.getGuid()+"</guid>");
                writer.write("<media:group>");

                for(RSSMedia eachRSSMedia : eachItem.getRssMediaList())
                {
                    writer.write("<media:content ");
                    writer.write("medium="+eachRSSMedia.getMedium());
                    writer.write("url="+eachRSSMedia.getUrl());
                    writer.write(" height="+eachRSSMedia.getHeight());
                    writer.write(" width="+eachRSSMedia.getWidth());
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
