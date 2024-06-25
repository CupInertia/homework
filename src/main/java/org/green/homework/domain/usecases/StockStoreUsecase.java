package org.green.homework.domain.usecases;

import java.util.UUID;
import org.green.homework.domain.Stock;

public interface StockStoreUsecase {
  void delete(UUID stockID);

  Stock update(Stock stock);

  Stock create(Stock stock);

  Stock getStock(UUID stockID);
}
