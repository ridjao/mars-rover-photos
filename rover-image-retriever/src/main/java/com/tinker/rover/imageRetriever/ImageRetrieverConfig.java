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
				String date = fields[0].trim();
				String format = "yyyy-MM-dd";
				if (fields.length > 1)
					format = fields[1].trim();
				
				dates.add(new SimpleDateFormat(format).parse(date));
			} 
		} finally {
			if (reader != null)
				reader.close();
		}
				
		return dates;
	}

}
