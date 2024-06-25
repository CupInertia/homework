package org.green.homework.adapter.out.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  Clock clock() {
    return Clock.systemDefaultZone();
  }
}
