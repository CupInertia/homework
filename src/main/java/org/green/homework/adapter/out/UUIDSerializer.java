package org.green.homework.adapter.out;

import java.util.UUID;
import org.apache.kafka.common.serialization.Serializer;

public class UUIDSerializer implements Serializer<UUID> {

  @Override
  public byte[] serialize(String topic, UUID data) {
    return data.toString().getBytes();
  }
}
