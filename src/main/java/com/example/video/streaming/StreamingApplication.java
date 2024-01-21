package com.example.video.streaming;

import com.example.video.streaming.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class StreamingApplication {

  public static void main(String[] args) {
    SpringApplication.run(StreamingApplication.class, args);
  }
}
