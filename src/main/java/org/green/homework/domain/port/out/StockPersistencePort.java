package org.green.homework.domain.port.out;

import java.util.UUID;
import org.green.homework.domain.Stock;

public interface StockPersistencePort {
  public Stock create(Stock stock);

  public void delete(UUID id);

  public Stock update(Stock stock);

  public Stock getStock(UUID stockID);
}
