package com.example.suggestusername.model;

import com.example.suggestusername.config.YAMLConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Optional;
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

	public boolean isValid() {
		return isValid;
	}

	public Set<String> getSuggestions() {
		return suggestions;
	}

	public void checkInput(String input) throws IllegalArgumentException, IOException {
		input = input.trim();
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

		File usernamesFile = new ClassPathResource(filePath + "usernames.txt").getFile();
		this.existingNames.addAll(FileUtils.readLines(usernamesFile, Charset.defaultCharset()));

		String finalInput = input;
		Optional<String> foundOptional = this.existingNames.stream().filter(existingName ->
				StringUtils.equals(finalInput, existingName) || isContainedInSet(finalInput, this.restrictedWords))
				.findAny();

		if (foundOptional.isPresent()) {
			// suggest
			this.isValid = false;
			this.suggestions = generateSuggestions(finalInput);
		} else {
			// add to file
			FileUtils.writeStringToFile(usernamesFile, System.lineSeparator() + finalInput, Charset.defaultCharset(), true);
			this.isValid = true;
		}
	}

	private Set<String> generateSuggestions(String input) {

		Set<String> suggested = new TreeSet<>();
		String tempInput;

		int i = 0;
		while (i < 7) { // so that number suggestions reach a theoretical end
			tempInput = input.concat(String.valueOf(i));

			// Verify if input is contained in the restricted list or in the existing list
			// if it's false, add to suggestion list and increase i
			if (!isContainedInSet(tempInput, this.restrictedWords) && !isContainedInSet(tempInput, this.existingNames)) {
				suggested.add(tempInput);
				i++;
			}
		}

		/*i = 0;
		while (i < 7) {
			// given the input, take the first character and concatenate it to the existing string.
			// on the second loop, take 2 characters and so on
			tempInput = input.substring(0, input.length() - i);

			i++;
		}*/

		return suggested;
	}

	private boolean isContainedInSet(String word, Set<String> setToCheck) {
		for (String wordToCheck : setToCheck) {
			if (StringUtils.contains(word, wordToCheck)) {
				return true;
			}
		}

		return false;
	}
}
