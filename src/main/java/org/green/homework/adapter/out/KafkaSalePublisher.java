package org.green.homework.adapter.out;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.green.homework.domain.Sale;
import org.green.homework.domain.port.out.SalePublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSalePublisher implements SalePublisherPort {

  private final KafkaTemplate<UUID, Sale> kafkaTemplate;

  @Override
  public void publish(Sale sale) {
    kafkaTemplate.send("sales", sale);
  }
}
