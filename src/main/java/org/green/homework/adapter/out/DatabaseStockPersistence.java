package org.green.homework.adapter.out;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.green.homework.domain.Stock;
import org.green.homework.domain.StockNotFoundException;
import org.green.homework.domain.port.out.StockPersistencePort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseStockPersistence implements StockPersistencePort {

  private final StockRepository stockRepository;

  @Override
  public Stock create(Stock stock) {
    return convert(stockRepository.save(convert(stock)));
  }

  @Override
  public void delete(UUID id) {
    stockRepository.deleteByID(id.toString());
  }

  @Override
  public Stock update(Stock stock) {
    final var stockEntry = stockRepository.findByID(stock.getID().toString());
    stockEntry.setName(stock.getName());
    stockEntry.setPrice(stock.getPrice());
    stockEntry.setQuantity(stock.getQuantity());

    return convert(stockRepository.save(stockEntry));
  }

  @Override
  public Stock getStock(UUID stockID) {
    final var stock = stockRepository.findByID(stockID.toString());
    if (stock == null) throw new StockNotFoundException(stockID);

    return convert(stock);
  }

  private static Stock convert(StockEntity stock) {
    return Stock.builder()
        .ID(UUID.fromString(stock.getID()))
        .name(stock.getName())
        .price(stock.getPrice())
        .quantity(stock.getQuantity())
        .build();
  }

  private static StockEntity convert(Stock stock) {
    final var stockEntity = new StockEntity();
    stockEntity.setID(stock.getID().toString());
    stockEntity.setName(stock.getName());
    stockEntity.setPrice(stock.getPrice());
    stockEntity.setQuantity(stock.getQuantity());

    return stockEntity;
  }
}
