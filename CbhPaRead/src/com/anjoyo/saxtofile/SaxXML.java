﻿package com.anjoyo.saxtofile;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.anjoyo.finalltxt.Finaltxt;

public class SaxXML extends DefaultHandler {
	public static ArrayList<HashMap<String, String>> xmllist;
	HashMap<String, String> hashMap;

	public ArrayList<HashMap<String, String>> getArrayList() {
		return xmllist;
	}

	private final int resource = 1;
	private final int id = 2;
	private final int txtName = 3;
	private final int txtAuthor = 4;
	private final int txtImage = 5;
	private int elementType;

	public SaxXML() {
		xmllist = new ArrayList<HashMap<String, String>>();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String elementtval = new String(ch, start, length);
		switch (elementType) {
		case id:
			hashMap.put(Finaltxt.ID, elementtval);
			break;
		case txtName:
			hashMap.put(Finaltxt.TXTNAME, elementtval);
			break;
		case txtAuthor:
			hashMap.put(Finaltxt.TXTAUTHOR, elementtval);
			break;
		case txtImage:
			hashMap.put(Finaltxt.TXTIMAGE, elementtval);
			break;
		default:
			break;
		}
		elementType = 0;
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if (localName.equals(Finaltxt.RESOURCE)) {
			xmllist.add(hashMap);
			return;
		}
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	//	System.out.println("asd");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		//System.out.println("hhhhhh");
		if (localName.equals(Finaltxt.RESOURCE)) {
			hashMap = new HashMap<String, String>();
			return;

		}
		if (localName.equals(Finaltxt.ID)) {
			elementType = id;
			return;
		}
		if (localName.equals(Finaltxt.TXTNAME)) {
			elementType = txtName;
			return;
		}
		if (localName.equals(Finaltxt.TXTAUTHOR)) {
			elementType = txtAuthor;
			return;
		}
		if (localName.equals(Finaltxt.TXTIMAGE)) {
			elementType = txtImage;
			return;
		}
		elementType = 0;
	}

}
