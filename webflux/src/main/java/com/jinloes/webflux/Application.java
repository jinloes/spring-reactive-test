package com.jinloes.webflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController("/address")
@SpringBootApplication
public class Application {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final Random RANDOM = new Random();

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  public static final BlockingQueue<Runnable> QUEUE = new LinkedBlockingQueue<>();
  public static final Executor EXECUTOR = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS,
      QUEUE);
  public static final ScheduledExecutorService MONITOR = Executors.newScheduledThreadPool(1);

  @EventListener
  public void onApplicationStart(ApplicationReadyEvent event) {
    MONITOR.scheduleAtFixedRate(() -> {
      LOGGER.info("Queue size: {}", QUEUE.size());
    }, 0, 10, TimeUnit.SECONDS);
  }

  @PostMapping
  public Mono<ServerResponse> create(@RequestBody Mono<Address> address) {
    return address
        //.delayElement(Duration.ofSeconds(5))
        .subscribeOn(Schedulers.fromExecutor(EXECUTOR))
        .doOnNext(val -> {
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        })
        .doOnNext(val -> LOGGER.info("Address: {}", address))
        .flatMap(val -> ServerResponse.status(HttpStatus.CREATED).build());
  }

  public static class Address {
    private String name;
    private String address;

    public Address(String name, String address) {
      this.name = name;
      this.address = address;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }
  }
}
