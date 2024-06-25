package org.green.homework.domain.port.out;

import org.green.homework.domain.Sale;

public interface SalePublisherPort {
  void publish(Sale sale);
}
