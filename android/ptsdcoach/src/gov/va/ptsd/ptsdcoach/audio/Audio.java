package gov.va.ptsd.ptsdcoach.audio;

import android.net.Uri;

public class Audio {
	public Audio(Uri audioUril, String name, String artist) {
		super();
		this.audioUril = audioUril;
		this.name = name;
		this.artist = artist;
	}
	Uri audioUril;
	public Uri getAudioUril() {
		return audioUril;
	}
	public void setAudioUril(Uri audioUril) {
		this.audioUril = audioUril;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	String name;
	String artist;
	
}
