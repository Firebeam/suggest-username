package com.example.suggestusername.model;

import com.example.suggestusername.config.YAMLConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class SuggestionResult {

	private boolean isValid;
	private Set<String> suggestions;
	private YAMLConfig yamlConfig;

	public SuggestionResult(YAMLConfig yamlConfig) {
		this.yamlConfig = yamlConfig;
	}

	public void checkInput(String input) throws IllegalArgumentException {
		if (!StringUtils.isBlank(input)) {
			if (input.length() < yamlConfig.getMinInputLength()) {
				throw new IllegalArgumentException(String.format("Input must be greater than %d characters.",
						yamlConfig.getMinInputLength()));
			}
		}

	}
}
