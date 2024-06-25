package org.green.homework.domain;

import lombok.RequiredArgsConstructor;
import org.green.homework.domain.usecases.ReportSaleUsecase;
import org.green.homework.domain.usecases.StockStoreUsecase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoggingSalesReporter implements ReportSaleUsecase {

  private final StockStoreUsecase stockStore;

  private static final Logger logger = LoggerFactory.getLogger(LoggingSalesReporter.class);

  @Override
  public void report(Sale sale) {
    final var stock = stockStore.getStock(sale.getStockID());
    logger.info(
        String.format(
            "Sold: %s of %s (%s) for %s. %s left!",
            sale.getQuantity(),
            stock.getName(),
            stock.getID(),
            sale.getPrice(),
            stock.getQuantity()));
  }
}
