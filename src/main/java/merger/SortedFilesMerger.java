package merger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;

import merger.binary.ComplexBinaryNode;
import merger.binary.IBinaryNode;
import merger.binary.LeafBinaryNode;
import merger.iterator.FileIterator;

import org.apache.log4j.Logger;

public final class SortedFilesMerger {

	private static final Logger LOG = Logger.getLogger(SortedFilesMerger.class);

	private SortedFilesMerger() {
		super();
	}

	public static void merge(Path[] srcPaths, Path destPath) {

		FileIterator[] iterators = null;

		try {
			Files.deleteIfExists(destPath);
			Files.createFile(destPath);

			iterators = new FileIterator[srcPaths.length];

			Queue<IBinaryNode> nodes = new ArrayDeque<>();

			try (BufferedWriter destWriter = Files.newBufferedWriter(destPath,
					Charset.defaultCharset())) {

				for (int i = 0; i < srcPaths.length; i++) {
					/**
					 * check what will happen if one iterator is failed check if
					 * other streams will be safely closed in this case
					 */
					FileIterator it = new FileIterator(srcPaths[i]);
					iterators[i] = it;
					nodes.add(new LeafBinaryNode(it));
				}

				while (nodes.size() > 1) {
					IBinaryNode node1 = nodes.poll();
					IBinaryNode node2 = nodes.poll();
					nodes.add(new ComplexBinaryNode(node1, node2));
				}

				IBinaryNode rootNode = nodes.poll();

				String smallesValue = null;

				while ((smallesValue = rootNode.getValue()) != null) {
					destWriter.write(smallesValue);
					destWriter.newLine();
					rootNode.next();
				}
			}

		}
		catch (IOException ioEx) {
			LOG.error(ioEx);
		}
		finally {
			if (iterators != null) {
				for (int i = 0; i < iterators.length; i++) {
					try {
						iterators[i].close();
					}
					catch (Exception ex) {
						LOG.error("Can't properly close file", ex);
					}
				}
			}
		}

		LOG.info("SortedFilesMerger finished successfully");
	}

}
