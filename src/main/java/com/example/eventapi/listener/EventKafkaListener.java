package com.example.eventapi.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.eventapi.event.CreateKafkaEvent;
import com.example.eventapi.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventKafkaListener {
	private final KafkaService kafkaService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(CreateKafkaEvent event) throws JsonProcessingException {
		kafkaService.send(event.id().toString(), event.body());
	}
}
