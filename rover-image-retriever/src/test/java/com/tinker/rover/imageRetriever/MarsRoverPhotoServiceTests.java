package com.tinker.rover.imageRetriever;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import mockit.Expectations;
import mockit.Mocked;

class MarsRoverPhotoServiceTests {
	@Mocked Client mockClient;
	@Mocked WebTarget mockWebTarget;
	@Mocked Builder mockBuilder;
	@Mocked Response mockResponse;
	
	@Test
	void shouldRetrieveContentFromURLInJson() throws MalformedURLException, ParseException {		
		final String roverName = "curiosity";
		final String earthDate = "2015-06-03";
		final String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/" + roverName
				+ "/photos?earth_date=" + earthDate 
				+ "&api_key=DEMO_KEY";
		final String imageUrl = "http://mock.com/expected.jpg";
		final String json = "{\"photos\":[{\"id\":1,\"img_src\":\"" + imageUrl + "\"}]}";
		
		new Expectations() {{
			mockClient.target(url); result = mockWebTarget;			
			mockWebTarget.request(MediaType.APPLICATION_JSON); result = mockBuilder;			
			mockBuilder.get(); result = mockResponse;
			mockResponse.readEntity(String.class); result = json;						
		}};
		
		MarsRoverPhotoService rest = new MarsRoverPhotoService(mockClient);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<RoverImage> images = rest.getImages(roverName, formatter.parse(earthDate));
		assertEquals(imageUrl, images.get(0).getImageUrl());
	}
}
