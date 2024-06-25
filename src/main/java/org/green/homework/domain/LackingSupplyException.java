package org.green.homework.domain;

public class LackingSupplyException extends RuntimeException {
  public LackingSupplyException(Stock stock, long quantity) {
    super(
        String.format(
            "The requested quantity of %s exceeds the supply of: %s, which is %s!",
            quantity, stock.getName(), stock.getQuantity()));
  }
}
