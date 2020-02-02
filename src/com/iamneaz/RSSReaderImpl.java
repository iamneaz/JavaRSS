package com.iamneaz;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSReaderImpl implements RSSReader {


    public void runRSSReader(String urlAddress) throws IOException {

        List<RSSItems> entries = new ArrayList<>();
        List<String> items = new ArrayList<>();
        RSSHeader rssHeader = new RSSHeader();
        rssHeader = getHeaderItems(urlAddress);
        try {
            items = getItems(urlAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            FileWriter writer = new FileWriter("a.rss", true);
//            writer.write("Hello World");
//            writer.write("\r\n");   // write new line
//            writer.write("Good Bye!");
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        for (String string : items) {
            RSSItems rssItems = new RSSItems();
            rssItems.setTitle(getElement(string, "<title>", "</title>", false));
            rssItems.setLink(getElement(string, "<link>", "</link>", false));
            rssItems.setGuid(getElement(string, "<guid>", "</guid>", false));
            List<RSSMedia> rssMediaList = getMediaElements(string);
            rssItems.setRssMediaList(rssMediaList);
            entries.add(rssItems);

        }
//
//
//        for (RSSItems rssItems : entries) {
//            System.out.println(rssItems.getTitle());
//            System.out.println(rssItems.getGuid());
//            System.out.println(rssItems.getLink());
//            List<RSSMedia> rssMediaList = rssItems.getRssMediaList();
//            for (RSSMedia rssMedia : rssMediaList) {
//                System.out.println(rssMedia.getUrl());
//                System.out.println(rssMedia.getHeight());
//                System.out.println(rssMedia.getWidth());
//            }
//        }


        RSSFeed rssFeed = new RSSFeed();
        rssFeed.setRssHeader(rssHeader);
        rssFeed.setEntries(entries);

        try {
            FileWriter writer = new FileWriter("a.rss", true);
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




            writer.write("\r\n");   // write new line

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public List<String> getItems(String urlAddress) throws IOException {
        URL rssURL = new URL(urlAddress);
        BufferedReader in = new BufferedReader(new InputStreamReader(rssURL.openStream()));

        String line;
        List<String> items = new ArrayList<>();
        while ((line = in.readLine()) != null) {
            int itemEndIndex = 0;
            int itemStartIndex = 0;
            while (itemStartIndex >= 0) {
                itemStartIndex = line.indexOf("<item>", itemEndIndex);
                if (itemStartIndex >= 0) {
                    itemEndIndex = line.indexOf("</item>", itemStartIndex);
                    if (itemEndIndex >= 0) {

                        System.out.println(line.substring(itemStartIndex + "<item>".length(), itemEndIndex));
                        //sourceCode += line.substring(titleStartIndex + "<item>".length(), titleEndIndex) + "\n";
                        items.add(line.substring(itemStartIndex + "<item>".length(), itemEndIndex));
                    } else {
                        break;
                    }

                }
            }
        }
        in.close();
        return items;
    }

    public String getElement(String line, String startElement, String endElement, boolean xml) {
        String element = "";

        int elementStartIndex = 0;
        int elementEndIndex = 0;

        while (elementStartIndex >= 0) {
            elementStartIndex = line.indexOf(startElement, elementEndIndex);
            if (elementStartIndex >= 0) {
                elementEndIndex = line.indexOf(endElement, elementEndIndex);
                if (elementEndIndex >= elementStartIndex) {
                    if (xml) {
                        element = line.substring(elementStartIndex + startElement.length(), elementEndIndex + endElement.length());
                    } else {
                        element = line.substring(elementStartIndex + startElement.length(), elementEndIndex);
                    }

                } else {
                    break;
                }

            }
        }


        return element;
    }

    public List<RSSMedia> getMediaElements(String itemString) {
        List<RSSMedia> rssMediaList = new ArrayList<>();
        int titleEndIndex = 0;
        int titleStartIndex = 0;
        while (titleStartIndex >= 0) {
            titleStartIndex = itemString.indexOf("<media:", titleEndIndex);
            if (titleStartIndex >= 0) {
                titleEndIndex = itemString.indexOf("\" />", titleStartIndex);
                //System.out.println(line.substring(titleStartIndex + "<item>".length(), titleEndIndex));
                //sourceCode += line.substring(titleStartIndex + "<item>".length(), titleEndIndex) + "\n";
                String rssMediaString = itemString.substring(titleStartIndex + "<media:".length(), titleEndIndex);
                RSSMedia rssMedia = new RSSMedia();
                rssMedia.setUrl(getElement(rssMediaString, "url=\"", "\" height", false));
                rssMedia.setHeight(getElement(rssMediaString, "height=\"", "\" width", false));
                rssMedia.setWidth(getElement(rssMediaString, "width=\"", "\" type", false));
                //rssMedia.setType(getElement(rssMediaString,"type=\"","\" />"));
                rssMediaList.add(rssMedia);
            }
        }

        return rssMediaList;

    }

    public RSSHeader getHeaderItems(String urlAddress) throws IOException {
        URL rssURL = new URL(urlAddress);
        BufferedReader in = new BufferedReader(new InputStreamReader(rssURL.openStream()));
        RSSHeader rssHeader = new RSSHeader();
        String line;
        while ((line = in.readLine()) != null) {
            int itemEndIndex ;
            int itemStartIndex = 0;
            while (itemStartIndex >= 0) {
                //itemStartIndex = line.indexOf("<", itemEndIndex);
                if (itemStartIndex >= 0) {
                    itemEndIndex = line.indexOf("/><item>", itemStartIndex);
                    if (itemEndIndex >= itemStartIndex) {

                        line = line.substring(itemStartIndex, itemEndIndex);
                        String[] xml = new String[2];
                        xml[0] = getElement(line, "<?xml", "xsl\"?>", true).replace("?>", "");
                        xml[1] = getElement(line, ".xsl\"?><?xml", "css\"?>", true).replace("?>", "");
                        rssHeader.setXml(xml);
                        rssHeader.setRss(getElement(line, "<rss ", "><channel>", false));
                        rssHeader.setTitle(getElement(line, "<title>", "</title>", false));
                        rssHeader.setDescription(getElement(line, "<description>", "</description>", false));
                        rssHeader.setLink(getElement(line, "<link>", "</link>", false));
                        rssHeader.setImageURL(getElement(line, "<image><url>", "</url>", false));
                        rssHeader.setImageTitle(getElement(line, "</url><title>", "</title>", false));
                        rssHeader.setImageLink(getElement(line, "</title><link>", "</link>", false));
                        rssHeader.setGenerator(getElement(line, "<generator>", "</generator>", false));
                        rssHeader.setCopyright(getElement(line, "<copyright>", "</copyright>", false));
                        rssHeader.setLastBuildDate(getElement(line, "<lastBuildDate>", "</lastBuildDate>", false));
                        rssHeader.setPubDate(getElement(line, "<pubDate>", "</pubDate>", false));
                        rssHeader.setLanguage(getElement(line, "<language>", "</language>", false));
                        rssHeader.setTtl(getElement(line, "<ttl>", "</ttl>", false));
                        String[] atom = new String[2];
                        atom[0]=getElement(line, "</ttl><atom10:link", "/><feedburner:info", false);
                        atom[1]=getElement(line, "\" /><atom10:link", ".com/\"", true);
                        rssHeader.setAtom10(atom);
                        rssHeader.setFeedBurner(getElement(line, "<feedburner:info", "/><atom10:link", false));
                    } else {
                        //System.out.println("break");
                        break;
                    }

                }
            }
        }
        in.close();
        return rssHeader;
    }
}
