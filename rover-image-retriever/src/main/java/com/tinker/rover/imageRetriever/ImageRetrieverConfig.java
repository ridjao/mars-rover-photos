package com.tinker.rover.imageRetriever;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageRetrieverConfig {
	private String filename;

	public ImageRetrieverConfig(String filename) {
		this.filename = filename;
	}

	public List<Date> getDates() throws Exception {
		List<Date> dates = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line; 
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				
				String[] fields = line.split(":");
				if (fields.length < 2)
					throw new Exception("Invalid line [" + line + "] in config file");
				
				dates.add(new SimpleDateFormat(fields[1].trim()).parse(fields[0].trim()));
			} 
		} finally {
			if (reader != null)
				reader.close();
		}
				
		return dates;
	}

}
