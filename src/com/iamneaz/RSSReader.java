package com.iamneaz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RSSReader {


    public void runRSSReader(String urlAddress)
    {
        List<RSSHeader> entries = new ArrayList<>();
        List<String> items = new ArrayList<>();
        try {
            URL rssURL  = new URL(urlAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(rssURL.openStream()));

            String sourceCode="";
            String line;
//            while((line = in.readLine())!=null)
//            {
//                int titleEndIndex = 0;
//                int titleStartIndex = 0;
//                while (titleStartIndex >= 0)
//                {
//                    titleStartIndex = line.indexOf("<item>", titleEndIndex);
//                    if (titleStartIndex >= 0)
//                    {
//                        titleEndIndex = line.indexOf("</item>", titleStartIndex);
//                        sourceCode += line.substring(titleStartIndex + "<item>".length(), titleEndIndex).replace("<![CDATA[","").replace("]]>","") + "\n";
////                        String itemString = line.substring(titleStartIndex + "<item>".length(), titleEndIndex)+"\n";
////                        items.add(itemString);
////                        System.out.println(itemString);
////                        RSSHeader rssHeader = new RSSHeader();
////                        rssHeader.setTitle(getElement(itemString,"<title>","</title>"));
////                        entries.add(rssHeader);
//                    }
//                }
//            }

            while ((line = in.readLine()) != null)
            {
                int titleEndIndex = 0;
                int titleStartIndex = 0;
                while (titleStartIndex >= 0)
                {
                    titleStartIndex = line.indexOf("<item>", titleEndIndex);
                    if (titleStartIndex >= 0)
                    {
                        titleEndIndex = line.indexOf("</item>", titleStartIndex);
                        //System.out.println(line.substring(titleStartIndex + "<item>".length(), titleEndIndex));
                        //sourceCode += line.substring(titleStartIndex + "<item>".length(), titleEndIndex) + "\n";
                        items.add(line.substring(titleStartIndex + "<item>".length(), titleEndIndex));
                    }
                }
            }

            in.close();

            for(String string : items)
            {
                RSSHeader rssHeader = new RSSHeader();
                rssHeader.setTitle(getElement(string,"<title>","</title>"));
                rssHeader.setLink(getElement(string,"<link>","</link>"));
                rssHeader.setGuid(getElement(string,"<guid>","</guid>"));
                List<RSSMedia> rssMediaList = getMediaElements(string);
                rssHeader.setRssMediaList(rssMediaList);
                entries.add(rssHeader);

            }



            for(RSSHeader rssHeader : entries)
            {
                System.out.println(rssHeader.getTitle());
                System.out.println(rssHeader.getGuid());
                System.out.println(rssHeader.getLink());
                List<RSSMedia> rssMediaList = rssHeader.getRssMediaList();
                for(RSSMedia rssMedia : rssMediaList)
                {
                    System.out.println(rssMedia.getUrl());
                    System.out.println(rssMedia.getHeight());
                    System.out.println(rssMedia.getWidth());
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Malformed URL");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong reading the contents");
        }



    }

    public String getElement(String line,String startElement,String endElement)
    {
        String element="";

        int elementStartIndex = 0;
        int elementEndIndex = 0;

        while (elementStartIndex >= 0)
        {
            elementStartIndex = line.indexOf(startElement, elementEndIndex);
            if (elementStartIndex >= 0)
            {
                elementEndIndex = line.indexOf(endElement, elementEndIndex);
                element = line.substring(elementStartIndex + startElement.length(), elementEndIndex).trim();
            }
        }


        return element;
    }

    public List<RSSMedia> getMediaElements(String itemString)
    {
        List<RSSMedia> rssMediaList = new ArrayList<>();
        int titleEndIndex = 0;
        int titleStartIndex = 0;
        while (titleStartIndex >= 0)
        {
            titleStartIndex = itemString.indexOf("<media:", titleEndIndex);
            if (titleStartIndex >= 0)
            {
                titleEndIndex = itemString.indexOf("\" />", titleStartIndex);
                //System.out.println(line.substring(titleStartIndex + "<item>".length(), titleEndIndex));
                //sourceCode += line.substring(titleStartIndex + "<item>".length(), titleEndIndex) + "\n";
                String rssMediaString = itemString.substring(titleStartIndex + "<media:".length(), titleEndIndex);
                RSSMedia rssMedia = new RSSMedia();
                rssMedia.setUrl(getElement(rssMediaString,"url=\"","\" height"));
                rssMedia.setHeight(getElement(rssMediaString,"height=\"","\" width"));
                rssMedia.setWidth(getElement(rssMediaString,"width=\"","\" type"));
                //rssMedia.setType(getElement(rssMediaString,"type=\"","\" />"));
                rssMediaList.add(rssMedia);
            }
        }

        return rssMediaList;

    }
}
