package com.tinker.rover.imageRetriever;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

public class RoverImageTests {
	@Mocked Client mockClient;
	@Mocked WebTarget mockWebTarget;
	@Mocked Builder mockBuilder;
	@Mocked Response mockResponse;
	@Mocked FileUtils fileUtils;
	@Mocked Files files;
	@Test
	public void shouldStoreRetrievedImages() throws Exception {
		final long utcDate = 0;
		final String dateIndex = new Date(utcDate).toString();
		final String expectedFilename1 = "expectedFilename1";
		final String url1 = "url1";
		final byte[] expectedContents1 = "anyString1".getBytes();
		Map<String, List<RoverImage>> contentMap = new HashMap<>();
		List<RoverImage> images = new ArrayList<>();
		images.add(new RoverImage(expectedFilename1, url1));
		
		final String expectedFilename2 = "expectedFilename2";
		final String url2 = "url2";
		final byte[] expectedContents2 = "anyString2".getBytes();
		images.add(new RoverImage(expectedFilename2, url2));
		contentMap.put(dateIndex, images);

		new Expectations() {{
			Files.exists((Path)any); result = false;			
			mockClient.target(anyString); result = mockWebTarget;
			mockWebTarget.request(MediaType.APPLICATION_OCTET_STREAM); result = mockBuilder;			
			mockResponse.readEntity(InputStream.class); returns (new ByteArrayInputStream(expectedContents1),
					new ByteArrayInputStream(expectedContents2));
		}};
		
		MarsRoverImageSource dataSource = new MockDateIndexedDataSource(contentMap);
		RoverImages remoteImages = new RoverImages(dataSource, new ImageWriter(mockClient,""));
		List<String> filenames = remoteImages.download("", new Date(utcDate));

		assertEquals(filenames.size(), images.size());
		new Verifications() {{
			FileUtils.writeByteArrayToFile(new File(expectedFilename1 + ".jpg"), expectedContents1);
			FileUtils.writeByteArrayToFile(new File(expectedFilename2 + ".jpg"), expectedContents2);
		}};				
	}

	private static class MockDateIndexedDataSource implements MarsRoverImageSource {
		Map<String, List<RoverImage>> contentMap;

		public MockDateIndexedDataSource(Map<String, List<RoverImage>> contentMap) {
			this.contentMap = contentMap;
		}

		@Override
		public List<RoverImage> getImages(String roverName, Date index) {
			return contentMap.get(index.toString());
		}
	}
}
