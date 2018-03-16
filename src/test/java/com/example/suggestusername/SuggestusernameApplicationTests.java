package com.example.suggestusername;

import com.example.suggestusername.config.YAMLConfig;
import com.example.suggestusername.model.SuggestionResult;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = { "classpath:application-test.yml" })
public class SuggestusernameApplicationTests {

	@Autowired
	private YAMLConfig yamlConfig;

	private SuggestionResult suggestionResult;
	private static File usernameFile;

	@BeforeClass
	public static void setUp() throws IOException {
		usernameFile = new ClassPathResource("usernames" + File.separator + "usernames.txt").getFile();
	}

	@Before
	public void testSetUp() throws IOException {
		suggestionResult = new SuggestionResult(yamlConfig);

		// add basic seeds
		suggestionResult.checkInput("existing");
		suggestionResult.checkInput("existing2");
		suggestionResult.checkInput("existingex");
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void addNonExisting() throws IOException {
		suggestionResult.checkInput("nonExisting");

		assertTrue(suggestionResult.isValid());
		assertTrue(FileUtils.readLines(usernameFile, Charset.defaultCharset()).contains("nonExisting".toLowerCase()));
	}

	@After
	public void tearDown() throws IOException {
		FileUtils.write(usernameFile, "", Charset.defaultCharset());
	}
}
