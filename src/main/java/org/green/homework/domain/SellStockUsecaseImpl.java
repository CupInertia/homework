package org.green.homework.domain;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.green.homework.domain.port.out.SalePersistencePort;
import org.green.homework.domain.port.out.StockPersistencePort;
import org.green.homework.domain.usecases.SellStockUsecase;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellStockUsecaseImpl implements SellStockUsecase {

  private final StockPersistencePort stockPersistencePort;
  private final SalePersistencePort salePersistencePort;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final Clock clock;
  private final IDCreator idCreator;

  @Transactional
  @Override
  public Sale sell(UUID stockID, long quantity) {
    final var stock = stockPersistencePort.getStock(stockID);

    if (quantity > stock.getQuantity()) {
      throw new LackingSupplyException(stock, quantity);
    }

    final var remainingStock =
        stockPersistencePort.update(
            stock.toBuilder().quantity(stock.getQuantity() - quantity).build());

    final var sale =
        salePersistencePort.save(
            Sale.builder()
                .ID(idCreator.create())
                .stockID(remainingStock.getID())
                .price(remainingStock.getPrice())
                .quantity(quantity)
                .time(OffsetDateTime.now(clock))
                .build());

    applicationEventPublisher.publishEvent(sale);

    return sale;
  }
}
