package com.defaultPackage.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.defaultPackage.model.User;

import reactor.core.publisher.Flux;

@Service
public class ChatService {

	@Autowired
	ChatClient chatClient;
	

	public Flux<String> chatWithAi(String userPrompt) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return chatClient
					     .prompt()
					     .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,user.getUsername()))
					     .user(userPrompt)
						 .stream()
						 .content();
		
		
	}

}
