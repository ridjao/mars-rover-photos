package com.tinker.rover.imageRetriever;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import mockit.Expectations;
import mockit.Mocked;

class ImageRetrieverConfigTest {
	@Mocked FileReader fileReader;
	@Mocked BufferedReader reader;
	@Test
	void shouldReadConfigFromFile() throws Exception {
		ImageRetrieverConfig config = new ImageRetrieverConfig("");
		
		new Expectations() {{
			reader.readLine(); returns("02/27/17:MM/dd/yy",
					"June 2, 2018:MMMM dd, yyyy",
					"Jul-13-2016:MMM-dd-yyyy",
					"",
					"April 31, 2018:MMMMM dd, yyyy",
					null);
		}};
		
		List<Date> dates = config.getDates();
		
		assertEquals(dates.size(), 4);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		assertEquals(formatter.parse("2017-02-27"), dates.get(0)); //02/27/17
		assertEquals(formatter.parse("2018-06-02"), dates.get(1)); //June 2, 2018
		assertEquals(formatter.parse("2016-07-13"), dates.get(2)); //Jul-13-2016
		assertEquals(formatter.parse("2018-04-31"), dates.get(3)); //April 31, 2018 => May 1, 2018
	}

}
