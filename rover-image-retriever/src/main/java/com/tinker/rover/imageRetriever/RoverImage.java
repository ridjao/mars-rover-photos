package com.tinker.rover.imageRetriever;

public class RoverImage {
	private String id;
	private String url;		
	public RoverImage(String id, String url) {		
		this.id = id;
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public String getImageUrl() {
		return url;
	}
}
