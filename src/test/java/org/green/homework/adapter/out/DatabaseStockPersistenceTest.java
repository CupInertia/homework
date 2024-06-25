package org.green.homework.adapter.out;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.green.homework.domain.Stock;
import org.green.homework.domain.StockNotFoundException;
import org.green.homework.domain.port.out.StockPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
public class DatabaseStockPersistenceTest {

  @Autowired StockPersistencePort stockPersistencePort;

  static final Stock stock =
      Stock.builder().ID(UUID.randomUUID()).name("A stock item!").price(10.5).quantity(100).build();

  @Test
  void storesStock() {
    final var newStock = stockPersistencePort.create(stock);

    assertThat(stockPersistencePort.getStock(stock.getID()), is(stock));
    assertThat(newStock, is(stock));
  }

  @Test
  void deletesStockById() {
    stockPersistencePort.create(stock);

    stockPersistencePort.delete(stock.getID());

    assertThrows(StockNotFoundException.class, () -> stockPersistencePort.getStock(stock.getID()));
  }

  @Test
  void returnsStockById() {
    stockPersistencePort.create(stock);

    final var foundStock = stockPersistencePort.getStock(stock.getID());

    assertThat(foundStock, is(stock));
  }

  @Test
  void updatesStock() {
    stockPersistencePort.create(stock);
    final var stockUpdate =
        stock.toBuilder()
            .name(stock.getName() + ", but new!")
            .price(stock.getPrice() + 1)
            .quantity(stock.getQuantity() + 1)
            .build();

    final var returnedStock = stockPersistencePort.update(stockUpdate);

    final var updated = stockPersistencePort.getStock(stockUpdate.getID());
    assertThat(updated, is(stockUpdate));
    assertThat(returnedStock, is(stockUpdate));
  }
}
