package com.zmide.lit.main;

import android.net.Uri;

import com.zmide.lit.ui.MainActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ResourceCatcher {
	private static MainActivity activity;
	private static ArrayList<Uri> videos = new ArrayList<>();
	private static ArrayList<Uri> audios = new ArrayList<>();
	private static ArrayList<Uri> pictures = new ArrayList<>();
	private static Pattern
			video = Pattern.compile(TYPE.VIDEO_MATCHER),
			audio = Pattern.compile(TYPE.AUDIO_MATCHER),
			picture = Pattern.compile(TYPE.PICTURE_MATCHER);
	
	/*
	public static void init(MainActivity mainActivity)
	{
		if (activity == null) {
			activity = mainActivity;
		}
	}
	*/
	
	public static void sendResource(Uri url) {
		if (video.matcher((url + "").toLowerCase()).find() && !videos.contains(url)) {
			videos.add(url);
		} else if (audio.matcher((url + "").toLowerCase()).find() && !audios.contains(url)) {
			audios.add(url);
		} else if (picture.matcher((url + "").toLowerCase()).find() && !pictures.contains(url)) {
			pictures.add(url);
		}
	}
	
	public static ArrayList<Uri> getResources(int type) {
		switch (type) {
			case TYPE.VIDEO:
				return videos;
			case TYPE.AUDIO:
				return audios;
			case TYPE.PICTURE:
				return pictures;
		}
		return new ArrayList<>();
	}
	
	public static void clearResources() {
		videos.clear();
		audios.clear();
		pictures.clear();
	}
	
	static class TYPE {
		final static int VIDEO = 0;
		final static int AUDIO = 1;
		final static int PICTURE = 2;
		final static String VIDEO_MATCHER = ".(mp4|m3u8|avi|mpg|mpeg|m4p|3gp|3gpp)[^a-z0-9]";
		final static String AUDIO_MATCHER = ".(wav|mp3|m4a|ogg|mid|wmv)[^a-z0-9]";
		final static String PICTURE_MATCHER = ".(png|jpg|jpeg|gif|webp|bmp)[^a-z0-9]";
	}
	
}
