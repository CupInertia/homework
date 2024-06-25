package org.green.homework.adapter.out;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.green.homework.adapter.out.config.MessagingConfig;
import org.green.homework.domain.Sale;
import org.green.homework.domain.port.out.SalePublisherPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EmbeddedKafka(topics = "sales")
@DirtiesContext
@ContextConfiguration(classes = {MessagingConfig.class, KafkaSalePublisher.class})
public class KafkaSalePublisherTest {

  @Autowired EmbeddedKafkaBroker kafkaBroker;

  @Autowired SalePublisherPort salePublisherPort;

  @Test
  void publishesSales() {
    final var consumerProps = KafkaTestUtils.consumerProps("homework", "false", kafkaBroker);
    consumerProps.put("spring.json.trusted.packages", "org.green.homework.domain");
    final var cf = new DefaultKafkaConsumerFactory<UUID, Sale>(consumerProps);
    cf.setValueDeserializer(new JsonDeserializer<Sale>());
    final var consumer = cf.createConsumer();
    kafkaBroker.consumeFromAllEmbeddedTopics(consumer);

    final var sale =
        Sale.builder()
            .ID(UUID.randomUUID())
            .price(1)
            .quantity(2)
            .time(OffsetDateTime.now(ZoneId.of("UTC")))
            .build();

    salePublisherPort.publish(sale);

    final var messages = new ArrayList<ConsumerRecord<UUID, Sale>>();
    KafkaTestUtils.getRecords(consumer).records("sales").iterator().forEachRemaining(messages::add);
    assertThat(messages, hasSize(1));
    assertThat(messages.get(0).value(), is(sale));
  }
}
