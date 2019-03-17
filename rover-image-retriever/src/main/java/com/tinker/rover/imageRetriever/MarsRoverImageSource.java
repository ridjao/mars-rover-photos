package com.tinker.rover.imageRetriever;

import java.util.Date;
import java.util.List;

public interface MarsRoverImageSource {
	List<RoverImage> getImages(String roverName, Date index);
}
