package org.green.homework.adapter.in;

import lombok.RequiredArgsConstructor;
import org.green.homework.domain.Sale;
import org.green.homework.domain.usecases.ReportSaleUsecase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaleReceiver {

  private final ReportSaleUsecase reportSale;

  @KafkaListener(topics = "sales", groupId = "homework")
  void receiveSale(Sale sale) {
    reportSale.report(sale);
  }
}
