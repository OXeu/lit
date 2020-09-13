package com.zmide.lit.bookmark;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Deque;

public class BookmarkElement {

	public String name;
	public String URL;
	public String description;
	public String date;
	public boolean isFolder;
	public ArrayList<BookmarkElement> child = new ArrayList<>();

	@NonNull
	@Override
	public String toString() {
		String sep = " ; ";
		return "\"" + name + "\"" + sep
			+ URL + sep
			+ "\"" + description + "\"" + sep
			+ "\"" + date + "\"" + sep
			+ "\"" + child.toArray().toString() + "\"";

		//return tag.size() + " " + s;
		//return String.join(".",tag);
	}

	void addDeque(Deque<BookmarkElement> deque) {
		child = new ArrayList<>();
		child.addAll(deque);
	}
}
