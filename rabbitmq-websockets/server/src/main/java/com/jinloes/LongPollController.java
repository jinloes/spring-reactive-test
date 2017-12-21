package com.jinloes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by jinloes on 1/25/17.
 */
@RestController
public class LongPollController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@RequestMapping(value = "/long_poll", method = RequestMethod.GET)
	public DeferredResult<ResponseEntity<String>> getMessages() {
		DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
		CompletableFuture.supplyAsync(() -> {
			byte[] message = (byte[]) rabbitTemplate.receiveAndConvert("test", TimeUnit.SECONDS.toMillis(60));
			LOGGER.info("Message: " + new String(message));
			return new ResponseEntity<>(new String(message), HttpStatus.OK);
		}).whenCompleteAsync((result, throwable) -> {
			deferredResult.setResult(result);
		});
		deferredResult.onCompletion(() -> LOGGER.info("DONE"));
		return deferredResult;
	}
}
