package com.example.suggestusername;

import com.example.suggestusername.config.YAMLConfig;
import com.example.suggestusername.model.SuggestionResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SuggestusernameApplicationTests {

	@Autowired
	private YAMLConfig yamlConfig;

	private SuggestionResult suggestionResult;

	@Before
	public void setUp() throws IOException {
		suggestionResult = new SuggestionResult(yamlConfig);
	}

	@Test
	public void contextLoads() {
	}



}
