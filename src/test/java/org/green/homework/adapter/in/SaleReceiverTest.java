package org.green.homework.adapter.in;

import static org.mockito.Mockito.verify;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.green.homework.adapter.out.config.MessagingConfig;
import org.green.homework.domain.Sale;
import org.green.homework.domain.usecases.ReportSaleUsecase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled // TODO: Check why the listener doesn't handle messages.
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MessagingConfig.class, SaleReceiver.class})
@EmbeddedKafka(topics = "sales")
@DirtiesContext
public class SaleReceiverTest {

  @Autowired EmbeddedKafkaBroker kafkaBroker;

  @MockBean ReportSaleUsecase reportSale;

  @Test
  void reportsSales() throws InterruptedException, ExecutionException {
    final var props = KafkaTestUtils.producerProps(kafkaBroker);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put("spring.json.trusted.packages", "org.green.homework.domain");
    final var factory = new DefaultKafkaProducerFactory<UUID, Sale>(props);
    final var template = factory.createProducer();
    final var sale = Sale.builder().ID(UUID.randomUUID()).build();

    template.send(new ProducerRecord<UUID, Sale>("sales", sale)).get();

    verify(reportSale).report(sale);
  }
}
