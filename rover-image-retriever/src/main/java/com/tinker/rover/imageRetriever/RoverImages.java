package com.tinker.rover.imageRetriever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoverImages {
	private MarsRoverImageSource imageSource;
	private ImageWriter imageWriter;
	
	public RoverImages(MarsRoverImageSource imageSource, ImageWriter imageWriter) {
		this.imageSource = imageSource;
		this.imageWriter = imageWriter;
	}

	public List<String> download(String roverName, Date index) throws IOException {
		List<RoverImage> images = imageSource.getImages(roverName, index);
		List<String> filenames = new ArrayList<>();
		for (RoverImage image : images) {
			filenames.add(imageWriter.write(image));
		}
		return filenames;
	}
}
