package com.tinker.rover.imageRetriever;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ImageWriter {
	private Client client;
	private Path dirPath;
	
	public ImageWriter(Client client, String dirPath) throws Exception {		
		this.client = client;
		this.dirPath = Paths.get(dirPath);
		
		if (!Files.exists(this.dirPath)) {
			Files.createDirectory(this.dirPath);
		} else if (!Files.isDirectory(this.dirPath)) {
			throw new Exception(this.dirPath.toString() + " is not a directory.");
		}
	}

	public String write(RoverImage image) {
		Path filename = Paths.get(dirPath.toString(), image.getId() + ".jpg");
		byte[] imageBytes = getImageBytes(image.getImageUrl());
		try {
			if (!Files.exists(filename))
				FileUtils.writeByteArrayToFile(new File(filename.toString()), imageBytes);
		} catch (IOException e) {
			System.out.println("Working Directory = " +
		              System.getProperty("user.dir"));
			System.out.println("Failed to write image " + filename + " due to: " + e.getMessage());
		}		
		return filename.toString();
	}
	
	private byte[] getImageBytes(String url) {
		Response response = client.target(url).request(MediaType.APPLICATION_OCTET_STREAM).get();
		InputStream image = response.readEntity(InputStream.class);
		byte[] imageBytes = null;
		try {
			imageBytes = IOUtils.toByteArray(image);
		} catch (IOException e) {
		}
		return imageBytes;
	}

}
