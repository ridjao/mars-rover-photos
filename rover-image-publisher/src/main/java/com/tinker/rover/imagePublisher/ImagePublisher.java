package com.tinker.rover.imagePublisher;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.tinker.rover.imageRetriever.ImageWriter;
import com.tinker.rover.imageRetriever.MarsRoverPhotoService;
import com.tinker.rover.imageRetriever.RoverImages;

@Path("/photos")
public class ImagePublisher {	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String publishPhotos(
			@Context ServletContext context,
			@DefaultValue("curiosity") @QueryParam("rover") String roverName,
			@DefaultValue("2015-06-03") @QueryParam("earthDate") String earthDate) throws Exception {

		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(earthDate);
		
		Client RESTclient = ClientBuilder.newClient();
		MarsRoverPhotoService service = new MarsRoverPhotoService(RESTclient);
		
		String docBase = context.getRealPath("/");
		RoverImages images = new RoverImages(service, new ImageWriter(RESTclient, docBase));
				
		System.out.println("Writing to " + docBase);
		List<String> filenames = images.download(roverName, date);
		Integer count = filenames.size(); 		
				
		String html =  "<html> " + 
		"<title>Image Publisher Photos</title>" + 
		"<body><h1>Got " + count + " photos by " + roverName + " on " + date +"</h1>";
		
		for (String filename : filenames) {
			String shortName = Paths.get(filename).getFileName().toString();
			html += "<img src=\"../" + shortName +"\" alt=\""+ shortName +"\">";
		}
		
		html += "</body>" +
		"</html> ";
		 
		 return html;
	}
}
