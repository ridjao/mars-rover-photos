package com.tinker.rover.imageRetriever;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MarsRoverPhotoService implements MarsRoverImageSource {
	private Client client;

	public MarsRoverPhotoService(Client client) {
		this.client = client;		
	}

	@Override
	public List<RoverImage> getImages(String roverName, Date index) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/" + roverName + "/photos?earth_date="
				+ format.format(index) + "&api_key=DEMO_KEY";
		Response response = client.target(url).request(MediaType.APPLICATION_JSON).get();
		String json = response.readEntity(String.class);
		return getImageMetadataFromJsonResponse(json);
	}

	private List<RoverImage> getImageMetadataFromJsonResponse(String json) {
		List<RoverImage> images = new ArrayList<>();
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			JSONArray photos = (JSONArray) jsonObject.get("photos");
			for (Object photo : photos) {
				JSONObject image = (JSONObject) photo;
				images.add(new RoverImage(((Long)image.get("id")).toString(), (String)image.get("img_src")));
			}						
		} catch (ParseException e) {

		}
		return images;
	}
}
