package org.green.homework.adapter.in;

import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.green.homework.domain.Stock;
import org.green.homework.domain.usecases.SellStockUsecase;
import org.green.homework.domain.usecases.StockStoreUsecase;
import org.openapitools.api.StockApi;
import org.openapitools.model.Sale;
import org.openapitools.model.SaleRequest;
import org.openapitools.model.StockUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

// TODO: Forgot HATEOAS.
@RestController
@RequiredArgsConstructor
public class StockController implements StockApi {

  private final StockStoreUsecase stockStore;
  private final StockConverter stockConverter;
  private final SellStockUsecase sellStockUsecase;

  @Override
  public ResponseEntity<org.openapitools.model.Stock> stockStockIDGet(String stockID) {
    final var stock = stockStore.getStock(UUID.fromString(stockID));

    return ResponseEntity.ok(stockConverter.convert(stock));
  }

  @Override
  public ResponseEntity<org.openapitools.model.Stock> stockStockIDPut(
      String itemID, StockUpdate stockUpdate) {
    final var updatedStock =
        stockStore.update(
            Stock.builder()
                .ID(UUID.fromString(itemID))
                .name(stockUpdate.getName())
                .price(stockUpdate.getPrice().doubleValue())
                .quantity(stockUpdate.getQuantity().longValue())
                .build());

    return ResponseEntity.ok(stockConverter.convert(updatedStock));
  }

  @Override
  public ResponseEntity<org.openapitools.model.Stock> stockPost(StockUpdate stockItem) {
    final var newStock =
        stockStore.create(
            Stock.builder()
                .name(stockItem.getName())
                .price(stockItem.getPrice().doubleValue())
                .quantity(stockItem.getQuantity().longValue())
                .build());

    return ResponseEntity.created(URI.create("/stock/" + newStock.getID()))
        .body(stockConverter.convert(newStock));
  }

  @Override
  public ResponseEntity<Void> stockStockIDDelete(String stockID) {
    stockStore.delete(UUID.fromString(stockID));
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Sale> stockStockIDSalesPost(String stockID, SaleRequest saleRequest) {
    final var sale =
        sellStockUsecase.sell(UUID.fromString(stockID), saleRequest.getQuantity().longValue());

    return ResponseEntity.created(
            URI.create("/stock/" + sale.getStockID() + "/sales/" + sale.getID()))
        .build();
  }
}
