package com.zmide.lit.bookmark;

import java.util.ArrayList;
import com.zmide.lit.util.DBC;
import com.zmide.lit.base.MApplication;

public class BookmarkExport {
		public final String OPEN_DIR = "<DL><p>";

		public final String CLOSE_DIR = "</DL><p>";

		/**
		 * @var string
		 */
		private String markup = "";

		/**
		 * Example of input array:
		 * ``` php
		 * [
		 *     'Bookmark name 1' => 'Bookmark URL 1',
		 *     'Bookmark name 2' => 'Bookmark URL 2',
		 *     'Directory name' => [
		 *         'Bookmark name 3' => 'Bookmark URL 3',
		 *         'Bookmark name 4' => 'Bookmark URL 4',
		 *     ],
		 * ]
		 * ```
		 * @param array $bookmarks
		 */
		public BookmarkExport()
		{
			BookmarkElement $bookmarks = DBC.getInstance(MApplication.getContext()).getBookMarks4Export();
			if ($bookmarks!=null) {
				setMarkup($bookmarks);
			}
		}

		public String toString()
		{
			return markup;
		}

		private void setMarkup(BookmarkElement $bookmarks)
		{
			markup += OPEN_DIR + "\n";
			for (BookmarkElement bookmark : $bookmarks.child) {
				if (bookmark.isFolder) {
					markup += getDirectoryMarkup(bookmark.name) + "\n";
					setMarkup(bookmark);
				} else {
					markup +=
                    getLinkMarkup(bookmark.name, bookmark.URL ,bookmark.date) + "\n";
				}
			}

			markup += CLOSE_DIR + "\n";
		}

		/**
		 * @param string $name
		 * @return string
		 */
		private String getDirectoryMarkup(String $name)
		{
			return "<DT><H3>" + $name + "</H3>";
		}

		/**
		 * @param string $name
		 * @param string $url
		 * @return string
		 */
		private String getLinkMarkup(String $name,String $url ,String date)
		{
			return "<DT><A HREF=\"" + $url + "\">" + $name + "</A>";
		}
    
}
