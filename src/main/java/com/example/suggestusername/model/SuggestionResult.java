package com.example.suggestusername.model;

import com.example.suggestusername.config.YAMLConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class SuggestionResult {

	private boolean isValid;
	private Set<String> suggestions;
	private Set<String> existingNames;
	private Set<String> restrictedWords;
	private YAMLConfig yamlConfig;

	private final String filePath = "usernames" + File.separator;

	public SuggestionResult(YAMLConfig yamlConfig) throws IOException {
		this.yamlConfig = yamlConfig;
		this.suggestions = new TreeSet<>();
		this.existingNames = new HashSet<>();
		this.restrictedWords = new HashSet<>(FileUtils.readLines(new ClassPathResource(filePath +
						"restricted.txt").getFile(), Charset.defaultCharset()));
	}

	public void checkInput(String input) throws IllegalArgumentException, IOException {
		if (!StringUtils.isBlank(input)) {
			if (input.length() < yamlConfig.getMinInputLength()) {
				throw new IllegalArgumentException(String.format("Input must be greater than %d characters.",
						yamlConfig.getMinInputLength()));
			}

			checkUsername(input);
		}
	}

	private void checkUsername(String input) throws IllegalArgumentException, IOException {
		input = input.toLowerCase();

		for (String restrictedWord : this.restrictedWords) {
			if (StringUtils.contains(input, restrictedWord)) {
				throw new IllegalArgumentException(String.format("The word contains a restricted word: %s",
						restrictedWord));
			}
		}

		this.existingNames.addAll(FileUtils.readLines(new ClassPathResource(filePath +
				"usernames.txt").getFile(), Charset.defaultCharset()));
	}
}
