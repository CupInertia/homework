package org.green.homework.domain;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.green.homework.domain.port.out.StockPersistencePort;
import org.green.homework.domain.usecases.StockStoreUsecase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockStoreUsecaseImpl implements StockStoreUsecase {

  private final IDCreator idCreator;

  private final StockPersistencePort stockPersistencePort;

  @Override
  public void delete(UUID stockID) {
    stockPersistencePort.delete(stockID);
  }

  @Override
  public Stock update(Stock stockID) {
    return stockPersistencePort.update(stockID);
  }

  @Override
  public Stock create(Stock stock) {
    return stockPersistencePort.create(stock.toBuilder().ID(idCreator.create()).build());
  }

  @Override
  public Stock getStock(UUID stockID) {
    return stockPersistencePort.getStock(stockID);
  }
}
