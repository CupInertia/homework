package org.green.homework.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Stock {
  private UUID ID;
  private String name;
  private long quantity;
  private double price;
}
