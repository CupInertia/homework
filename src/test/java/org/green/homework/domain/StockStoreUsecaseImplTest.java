package org.green.homework.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.green.homework.domain.port.out.StockPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StockStoreUsecaseImplTest {

  @Mock private IDCreator idCreator;

  @Mock private StockPersistencePort stockPersistencePort;

  private StockStoreUsecaseImpl stockStoreUsecase;

  private static final UUID id = UUID.randomUUID();
  private static final String name = "A name";
  private static final double price = 10.0;
  private static final long quantity = 100;

  private static final Stock existingStock =
      Stock.builder().ID(id).name(name).price(price).quantity(quantity).build();

  @BeforeEach
  void setUp() {
    stockStoreUsecase = new StockStoreUsecaseImpl(idCreator, stockPersistencePort);
  }

  @Test
  void copiesInfoAndAssignsID() {
    when(idCreator.create()).thenReturn(id);
    final var newStock = Stock.builder().name(name).price(price).quantity(quantity).build();
    final var expectedNewStock = newStock.toBuilder().ID(id).build();
    when(stockPersistencePort.create(expectedNewStock)).thenReturn(expectedNewStock);

    final var createdStock = stockStoreUsecase.create(newStock);

    assertThat(createdStock, is(expectedNewStock));
  }

  @Test
  void deletesStock() {
    stockStoreUsecase.delete(id);

    verify(stockPersistencePort).delete(id);
  }

  @Test
  void findsStock() {
    when(stockPersistencePort.getStock(id)).thenReturn(existingStock);

    final var stock = stockStoreUsecase.getStock(id);

    assertThat(stock, is(existingStock));
  }

  @Test
  void updatesStock() {
    final var stockUpdate =
        existingStock.toBuilder()
            .name(existingStock.getName() + "!")
            .price(existingStock.getPrice() + 1)
            .quantity(existingStock.getQuantity() + 1)
            .build();
    when(stockPersistencePort.update(stockUpdate)).thenReturn(stockUpdate);

    final var updatedStock = stockStoreUsecase.update(stockUpdate);

    assertThat(updatedStock, is(stockUpdate));
  }
}
