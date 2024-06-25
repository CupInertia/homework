package org.green.homework.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.green.homework.domain.port.out.SalePersistencePort;
import org.green.homework.domain.port.out.StockPersistencePort;
import org.green.homework.domain.usecases.SellStockUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class SellStockUsecaseImplTest {

  private SellStockUsecase sellStockUsecase;

  @Mock private StockPersistencePort stockPersistencePort;

  @Mock private SalePersistencePort salePersistencePort;

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @Mock private Clock clock;

  @Mock private IDCreator idCreator;

  private static final UUID stockID = UUID.randomUUID();
  private static final Stock stock =
      Stock.builder().ID(stockID).price(1).quantity(3).name("A stock item").build();

  private static final Instant timeOfSale = Instant.parse("1969-01-01T01:01:01.00Z");

  @BeforeEach
  void setUp() {
    sellStockUsecase =
        new SellStockUsecaseImpl(
            stockPersistencePort, salePersistencePort, applicationEventPublisher, clock, idCreator);
  }

  @Test
  void persistsSaleAndNotifiesAboutIt() {
    // given
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(timeOfSale);

    when(stockPersistencePort.getStock(stock.getID())).thenReturn(stock);

    final var saleID = UUID.randomUUID();
    when(idCreator.create()).thenReturn(saleID);

    final var expectedRemainingStock = stock.toBuilder().quantity(2).build();
    when(stockPersistencePort.update(expectedRemainingStock)).thenReturn(expectedRemainingStock);
    final var expectedSale =
        Sale.builder()
            .ID(saleID)
            .stockID(stockID)
            .price(stock.getPrice())
            .quantity(1)
            .time(OffsetDateTime.ofInstant(timeOfSale, ZoneId.of("UTC")))
            .build();
    when(salePersistencePort.save(expectedSale)).thenReturn(expectedSale);

    // when
    final var sale = sellStockUsecase.sell(stock.getID(), 1);

    // then
    assertThat(sale, is(expectedSale));
    verify(applicationEventPublisher).publishEvent(expectedSale);
  }

  @Test
  void throwsWhenSupplyIsExceeded() {
    when(stockPersistencePort.getStock(stock.getID())).thenReturn(stock);

    assertThrows(
        LackingSupplyException.class,
        () -> sellStockUsecase.sell(stock.getID(), stock.getQuantity() + 1));
  }
}
