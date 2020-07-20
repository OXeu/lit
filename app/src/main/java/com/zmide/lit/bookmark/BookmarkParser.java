package com.zmide.lit.bookmark;

import android.annotation.SuppressLint;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;

/**
 * Horrible, would have been better to use a Stream parser, the Netscape format is barely HTML.
 */
public class BookmarkParser {
	@SuppressLint("SimpleDateFormat")
	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private int indent = 0;
	private Bookmark bookmark = new Bookmark();
	private Element first;
	private Deque<String> currentTag = new ArrayDeque<>(20);
	private long date;
	private ArrayList<Bookmark> bookmarks = new ArrayList<>();
	
	
	public BookmarkParser(String theDoc) {
		Document doc = Jsoup.parse(theDoc, "UTF-8");
		first = doc.select("DL").first();
		date = System.currentTimeMillis();
	}
	
	public void parse() {
		dlElement(first.children());
	}
	
	public ArrayList<Bookmark> getResult() {
		return bookmarks;
	}
	
	private void dlElement(Elements elems) {
		indent++;
		for (Element elem : elems) {
			switch (elem.nodeName()) {
				case "dt":
					if (bookmark.URL != null) {
						bookmarks.add(bookmark);
						bookmark = new Bookmark();
						bookmark.addDeque(currentTag);
						//bookmark.tag = currentTag.size() + String.join(":", currentTag );
					}
					dtElement(elem.children());
					break;
				case "dd":
					//System.out.println("# Descr: " + elem.html());
					bookmark.description = elem.html();
					break;
				case "dl":
					dlElement(elem.children());
					break;
				case "p":
					continue;
				default:
					Log.e("Bookmark", "! No comprendo: " + elem.nodeName() + ": " + elem.html());
			}
		}
		if (--indent > 0) currentTag.removeLast();
	}
	
	private void dtElement(Elements elems) {
		for (Element elem : elems) {
			switch (elem.nodeName()) {
				case "h3":
					//System.out.println("# Tag: " + elem.ownText());
					currentTag.addLast(elem.ownText());
					bookmark.addDeque(currentTag);
					//bookmark.tag = currentTag.size() + String.join(":", currentTag);
					break;
				case "a":
					//System.out.println("# "+ indent + " Link: " + elem.html());
					bookmark.name = elem.html();
					bookmark.URL = elem.attr("href");
					long dv = Long.parseLong(elem.attr("add_date").equals("") ? date + "" : elem.attr("add_date"));   // http://stackoverflow.com/questions/539900/google-bookmark-export-date-format
					//if (dv > 10000000000l) {  dv = dv / 1000l; } else { dv = dv * 1000l;  }
					bookmark.date = format.format(new Date(dv));
					break;
				case "dl":
					dlElement(elem.children());
					break;
				case "p":
					continue;
				default:
					Log.e("Bookmark", "! No comprendo: " + elem.html());
			}
		}
	}
}
