package com.umkc.cs.pb.tweets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class GenerateTweets {

	/** Twitter stream is set up to collect JSON data */
	private TwitterStream streamData;
	private String[] search;
	FileOutputStream fileOutputStream;
	File outputFile;
	GenerateTweets getTwitter;
	int tweetCount;

	public GenerateTweets() {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("m5sdv8dg0aP7exWEqchlNI7CP");
		cb.setOAuthConsumerSecret("kRR8jJZyP03kBskIpe8gurtzjaIRm7KFxGCcrZAS0hMoku07xN");
		cb.setOAuthAccessToken("141897544-s18Jy1z7wpjuLIMDhpxkIUrkaeE6f1oyEz6lbPVv");
		cb.setOAuthAccessTokenSecret("2DIvDR7Zi9oemwbY7XlKSCuGbZgvhJ4zrEbFEil1h4gcs");
		cb.setJSONStoreEnabled(true);
		cb.setIncludeEntitiesEnabled(true);

		streamData = new TwitterStreamFactory(cb.build()).getInstance();

	}

	public void getTweets() throws InterruptedException {

		getTwitter = new GenerateTweets();

		getTwitter.startTwitter();
		Thread.sleep(20000);
		getTwitter.stopTwitter();
	}

	public void startTwitter() {
		try {
			outputFile = new File("collectedTweets02.json");
			fileOutputStream = new FileOutputStream(outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String searchString = "film,movie,oscars,awards,liveshows,party";
		search = searchString.split(",");
		for (int i = 0; i < search.length; i++) {
			search[i] = search[i].trim();
		}

		// define stream's listener
		streamData.addListener(listener);

		System.out.println("Getting the tweets");

		// Set up a filter to generate tweets based on above input keywords
		FilterQuery query = new FilterQuery().track(search);
		streamData.filter(query);
	}

	public void stopTwitter() {

		System.out.println("Shutting down Twitter stream");
		streamData.shutdown();

		try {
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	StatusListener listener = new StatusListener() {

		// The onStatus method is called every time a new tweet comes in.
		public void onStatus(Status status) {
			/*
			 * the raw JSON of a tweet
			 * System.out.println(status.getUser().getScreenName() + ": " +
			 * status.getText());
			 * 
			 * 
			 * System.out.println("timestamp : " +
			 * String.valueOf(status.getCreatedAt().getTime()));
			 */

			try {
				fileOutputStream.write(DataObjectFactory.getRawJSON(status)
						.getBytes());

				System.out.println(tweetCount++);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		/* This listener will ignore everything except for new tweets */
		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		}

		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		}

		public void onScrubGeo(long userId, long upToStatusId) {
		}

		public void onException(Exception ex) {
		}

		public void onStallWarning(StallWarning warning) {
		}
	};

	public static void main(String[] args) throws InterruptedException {

		GenerateTweets generateTweets = new GenerateTweets();
		generateTweets.getTweets();
	}
}