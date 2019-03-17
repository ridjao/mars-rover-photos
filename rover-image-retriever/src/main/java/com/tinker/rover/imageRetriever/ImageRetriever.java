package com.tinker.rover.imageRetriever;

import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ImageRetriever {
	public static void main(String[] args) throws Exception {
		
		if (args.length < 2) {
			System.out.println("Usage: java ImageRetriever <config file> <rover name>");
			return;
		}
				
		String configFilename = args[0];
		String roverName = args[1];
		
		ImageRetrieverConfig config = new ImageRetrieverConfig(configFilename);		
		List<Date> dates = config.getDates();
		
		Client RESTclient = ClientBuilder.newClient();
		MarsRoverPhotoService service = new MarsRoverPhotoService(RESTclient);
		RoverImages images = new RoverImages(service, new ImageWriter(RESTclient,"."));
				
		Integer count = 0;
		for (Date date : dates) {
			System.out.println("Downloading images taken by " + roverName + " on " + date + "...");
			count += images.download(roverName, date).size();
		}				
		System.out.println("Downloaded a total of " + count + " images");
	}
}