package com.max.algs.preprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 
 * C-like preprocessor.
 * 
 *
 */
public final class PreprocessorUtils {

	private static final Logger LOG = Logger.getLogger(PreprocessorUtils.class);

	private static final Pattern INCLUDE_REGEXP = Pattern
			.compile("^#include\\s+['\"<](.+)['\">]\\s*$");
	private static final Pattern DEFINE_REGEXP = Pattern
			.compile("^#define\\s+([\\w_]+)\\s+([\\w]+)\\s*$");
	private static final Pattern UNDEF_REGEXP = Pattern
			.compile("^#undef\\s+([\\w_]+)\\s*$");

	private final Path[] classpath;

	private final Set<String> handledFiles = new HashSet<>();
	private final Map<String, String> definedParams = new HashMap<>();

	public PreprocessorUtils(Path[] classpath) {
		super();
		this.classpath = classpath;
	}

	public void handle(Path in, Path out) {

		try {

			if (!Files.exists(in)) {
				throw new IllegalArgumentException("Input file doesn't exist: "
						+ in);
			}

			if (Files.exists(out)) {
				Files.delete(out);
			}

			Files.createFile(out);

			handledFiles.add(in.getFileName().toString());

			try (BufferedWriter writer = Files.newBufferedWriter(out,
					Charset.defaultCharset())) {
				doProcessing(in, writer);
			}
		}
		catch (IOException ioEx) {
			LOG.error("Preprocessing failed", ioEx);
		}
	}

	private void doProcessing(Path in, BufferedWriter writer)
			throws IOException {

		try (BufferedReader reader = Files.newBufferedReader(in,
				Charset.defaultCharset())) {
			String line;

			boolean multilineCommentsStarted = false;

			while ((line = reader.readLine()) != null) {

				if (line.contains("//")) {
					int commentStartIndex = line.indexOf("//");
					handleLine(line.substring(0, commentStartIndex), writer);
				}
				else
					if (line.contains("/*")) {
						int commentStartIndex = line.indexOf("/*");

						handleLine(line.substring(0, commentStartIndex), writer);

						int commentEndIndex = line.indexOf("*/");

						if (commentEndIndex != -1) {
							handleLine(line.substring(commentEndIndex + 2),
									writer);
						}
						else {
							multilineCommentsStarted = true;
						}
					}
					else
						if (multilineCommentsStarted) {

							int commentEndIndex = line.indexOf("*/");

							if (commentEndIndex != -1) {
								multilineCommentsStarted = false;
								handleLine(line.substring(commentEndIndex + 2),
										writer);
							}
						}
						else {
							handleLine(line, writer);
						}

			}
		}

	}

	private void handleLine(String line, BufferedWriter writer)
			throws IOException {
		line = line.trim();

		if (line.contains("#pragma")) {
			return;
		}

		if (line.contains("#undef")) {
			Matcher matcher = UNDEF_REGEXP.matcher(line);
			if (matcher.matches()) {
				String key = matcher.group(1);
				definedParams.remove(key);
			}
			return;
		}

		if (line.contains("#define")) {
			Matcher matcher = DEFINE_REGEXP.matcher(line);
			if (matcher.matches()) {
				String name = matcher.group(1);
				String value = matcher.group(2);
				definedParams.put(name, value);
			}
			return;
		}

		if (line.contains("#include")) {
			Matcher matcher = INCLUDE_REGEXP.matcher(line);

			if (matcher.matches()) {

				Path includedPath = Paths.get(matcher.group(1));

				handledFiles.add(includedPath.getFileName().toString());

				Path includedFullPath = findInludedFile(includedPath);

				doProcessing(includedFullPath, writer);
			}

			return;
		}

		if ("".equals(line)) {
			return;
		}

		writer.write(substituteDefines(line));
		writer.newLine();
	}

	private String substituteDefines(String line) {

		for (Map.Entry<String, String> definedEntry : definedParams.entrySet()) {
			if (line.contains(definedEntry.getKey())) {
				line = line.replace(definedEntry.getKey(),
						definedEntry.getValue());
			}
		}

		return line;
	}

	private Path findInludedFile(Path filePath) throws IOException {

		for (Path baseDir : classpath) {

			Path fullPath = baseDir.resolve(filePath);

			if (Files.exists(fullPath)) {
				return fullPath;
			}
		}

		throw new IllegalStateException("Can't find file: " + filePath);
	}

}
