package org.green.homework.domain;

import java.util.UUID;

public class StockNotFoundException extends RuntimeException {
  public StockNotFoundException(UUID stockID) {
    super("Stock not found: " + stockID.toString());
  }
}
