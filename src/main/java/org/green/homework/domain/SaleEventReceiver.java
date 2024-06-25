package org.green.homework.domain;

import lombok.RequiredArgsConstructor;
import org.green.homework.domain.port.out.SalePublisherPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class SaleEventReceiver {
  private final SalePublisherPort salePublisherPort;

  @TransactionalEventListener
  public void handleSaleEvent(Sale sale) {
    salePublisherPort.publish(sale);
  }
}
