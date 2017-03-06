package s3;

import java.io.ByteArrayInputStream;

import org.apache.log4j.Logger;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;

@SuppressWarnings("unused")
public final class S3Main {

	private static final Logger LOG = Logger.getLogger(S3Main.class);

	private final String BUCKET_NAME = "arp-assets-dev";
	private final String ACCESS_KEY = "AKIAJT574P2JAGNSXYKQ";
	private final String SECRET_KEY = "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";

	private S3Main() {

		AmazonS3 conn = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY,
				SECRET_KEY));

		// PUT file
		putTestObject(conn, "test1.txt", "test-1");
		putTestObject(conn, "test2.txt", "test-2");
		putTestObject(conn, "test3.txt", "test-3");

		// DeleteObjectRequest deleteReq = new DeleteObjectRequest(BUCKET_NAME,
		// "test/test1.txt");
		// conn.deleteObject(deleteReq);

		// printObjectsInBucket(BUCKET_NAME, "test", conn);

		LOG.info("Main completed");
	}

	private void putTestObject(AmazonS3 conn, String fileName, String content) {
		// PUT file
		byte[] data = content.getBytes();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.addUserMetadata("status", "active");
		metadata.addUserMetadata("tag", fileName);
		PutObjectResult putResult = conn.putObject(BUCKET_NAME + "/test",
				fileName, new ByteArrayInputStream(data), metadata);
		LOG.info(putResult);
	}

	private void printObjectsInBucket(String bucketName, String prefix,
			AmazonS3 conn) {

		ListObjectsRequest listReq = new ListObjectsRequest()
				.withBucketName(BUCKET_NAME);
		if (prefix != null) {
			listReq.withPrefix(prefix);
		}

		ObjectListing objects = conn.listObjects(listReq);

		LOG.info("====================" + bucketName
				+ (prefix == null ? "" : "/" + prefix) + "================== ");

		LOG.info(objects.getMaxKeys());

		for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			System.out.printf("%s \t\t %d bytes %n", objectSummary.getKey(),
					objectSummary.getSize());
		}

		LOG.info("============================================================= ");
	}

	void listAllBuckets(AmazonS3 conn) {
		LOG.info("=========================== Buckets =============================");

		for (Bucket bucket : conn.listBuckets()) {
			LOG.info(bucket.getName() + "\t"
					+ StringUtils.fromDate(bucket.getCreationDate()));
		}
		LOG.info("==================================================================");
	}

	public static void main(String[] args) {
		try {
			new S3Main();
		}
		catch (Exception ex) {
			LOG.error(ex);
		}

	}

}
