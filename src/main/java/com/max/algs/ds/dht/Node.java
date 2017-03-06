package com.max.algs.ds.dht;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

public class Node {

	private static final Logger LOG = Logger.getLogger(Node.class);

	private final String url;
	private final HttpClient httpClient;

	public Node(String url, HttpClient httpClient) {
		super();
		this.url = url;
		this.httpClient = httpClient;
	}

	public String getUrl() {
		return url;
	}

	public boolean remove(String key) {

		HttpDelete httpDelete = new HttpDelete(url + "/key/" + key);

		try {
			HttpResponse response = httpClient.execute(httpDelete);

			if (response.getStatusLine().getStatusCode() == 200) {
				return true;
			}
		}
		catch (IOException ex) {
			LOG.error(ex);
		}
		finally {
			httpDelete.releaseConnection();
		}

		return false;
	}

	public String getValue(String key) {

		HttpGet httpGet = new HttpGet(url + "/key/" + key);

		try {

			HttpResponse response = httpClient.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == 200) {
				return readLine(response);
			}
			else
				if (response.getStatusLine().getStatusCode() == 404) {
					return null;
				}
		}
		catch (IOException ex) {
			LOG.error(ex);
		}
		finally {
			httpGet.releaseConnection();
		}

		return null;
	}

	public boolean put(String key, String value, int bucketIndex) {

		HttpPost httpPost = new HttpPost(url + "/key/" + key);

		try {

			httpPost.setEntity(new StringEntity(value + "," + bucketIndex));

			HttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200) {
				return true;
			}

		}
		catch (IOException ex) {
			LOG.error(ex);
		}
		finally {
			httpPost.releaseConnection();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Node other = (Node) obj;

		return com.google.common.base.Objects.equal(url, other.url);
	}

	/**
	 * This method is used when node is deleted and a range of keys should be
	 * merged with the previous node.
	 * 
	 * @param nodeToRemove
	 */
	public void mergeData(Node nodeToRemove) {
	}

	/**
	 * This method used when new node is added and a range of keys should be
	 * remapped.
	 * 
	 * Possible cases: 1. startIndex = 20, endIndex = 50 2. startIndex = 350,
	 * endIndex = 20 (circle boundary)
	 * 
	 */

	public void remap(Node newNode, int startIndex, int endIndex) {

		String entryResp = null;

		LOG.info(String.format(
				"Remapping keys in range [%d, %d] from '%s' to '%s'",
				startIndex, endIndex, getUrl(), newNode.getUrl()));

		HttpPost httpPost = new HttpPost(url + "/remap");

		try {
			httpPost.setEntity(new StringEntity(String.format("%d,%d",
					startIndex, endIndex)));

			HttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new IllegalStateException(String.format(
						"Can't remap keys range [%d, %d] for node '%s'",
						startIndex, endIndex, url));
			}

			entryResp = readLine(response).trim();
		}
		catch (IOException ex) {
			LOG.error(ex);
		}
		finally {
			httpPost.releaseConnection();
		}

		if (!"".equals(entryResp)) {

			String[] entries = entryResp.split("[|]");

			for (String entry : entries) {
				String[] keyVal = entry.split("[,]");
				newNode.put(keyVal[0], keyVal[1], Integer.valueOf(keyVal[2]));
			}
		}

	}

	private String readLine(HttpResponse response) throws IOException {
		try (InputStreamReader inReader = new InputStreamReader(response
				.getEntity().getContent());
				BufferedReader bufferedReader = new BufferedReader(inReader)) {
			return bufferedReader.readLine();
		}
	}

}
