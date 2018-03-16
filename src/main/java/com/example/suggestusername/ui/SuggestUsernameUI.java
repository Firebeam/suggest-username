package com.example.suggestusername.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SpringUI
public class SuggestUsernameUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		setContent(new TextField("place a username here"));
	}
}
