package com.defaultPackage.controller.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.defaultPackage.service.ChatService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user/chat")
public class ChatController {
	
	@Autowired
	ChatService serviceImpl;
	
	
	
	@GetMapping("/chatWithModel")
	public Flux<String> chatWithModel(@RequestParam("query") String userPrompt) {
		return serviceImpl.chatWithAi(userPrompt);
	}
}