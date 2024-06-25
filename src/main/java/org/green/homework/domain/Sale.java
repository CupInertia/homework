package org.green.homework.domain;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Jacksonized
public class Sale {
  private UUID ID;
  private UUID stockID;
  private double price;
  private long quantity;
  private OffsetDateTime time;
}
