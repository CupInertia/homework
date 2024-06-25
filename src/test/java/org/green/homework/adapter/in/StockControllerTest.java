package org.green.homework.adapter.in;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.green.homework.domain.Sale;
import org.green.homework.domain.StockNotFoundException;
import org.green.homework.domain.usecases.SellStockUsecase;
import org.green.homework.domain.usecases.StockStoreUsecase;
import org.junit.jupiter.api.Test;
import org.openapitools.model.SaleRequest;
import org.openapitools.model.StockUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StockController.class)
@Import({StockConverter.class})
public class StockControllerTest {

  @Autowired MockMvc endpoint;

  @Autowired ObjectMapper objectMapper;

  @MockBean StockStoreUsecase stockStoreUsecase;

  @Autowired StockConverter stockItemConverter;

  static final UUID stockID = UUID.randomUUID();

  @MockBean private SellStockUsecase sellStockUsecase;

  static final UUID id = UUID.randomUUID();
  static final String name = "Stock";
  static final long price = 1;
  static final long quantity = 3;

  @Test
  void returnsStockItemByID() throws Exception {
    final var stockItem =
        org.green.homework.domain.Stock.builder()
            .ID(id)
            .name(name)
            .price(price)
            .quantity(quantity)
            .build();
    when(stockStoreUsecase.getStock(id)).thenReturn(stockItem);

    final var expectedStockItem = stockItemConverter.convert(stockItem);

    endpoint
        .perform(get("/stock/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedStockItem)));
  }

  @Test
  void returns404WhenItemIsMissing() throws Exception {
    when(stockStoreUsecase.getStock(id)).thenThrow(StockNotFoundException.class);

    endpoint.perform(get("/stock/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void stocksNewItem() throws Exception {
    final var newStockItem =
        org.green.homework.domain.Stock.builder()
            .name(name)
            .price(price)
            .quantity(quantity)
            .build();

    when(stockStoreUsecase.create(newStockItem))
        .thenReturn(newStockItem.toBuilder().ID(id).build());

    endpoint
        .perform(
            post("/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new StockUpdate()
                            .name(name)
                            .price(BigDecimal.valueOf(price))
                            .quantity(BigDecimal.valueOf(quantity)))))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/stock/" + id));
  }

  @Test
  void updatesStockItem() throws Exception {
    final var stockItemUpdate =
        org.green.homework.domain.Stock.builder()
            .ID(id)
            .name("A new name!")
            .price(1)
            .quantity(10)
            .build();

    when(stockStoreUsecase.update(stockItemUpdate)).thenReturn(stockItemUpdate);

    endpoint
        .perform(
            put("/stock/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new StockUpdate()
                            .name(stockItemUpdate.getName())
                            .price(BigDecimal.valueOf(stockItemUpdate.getPrice()))
                            .quantity(BigDecimal.valueOf(stockItemUpdate.getQuantity())))))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    objectMapper.writeValueAsString(stockItemConverter.convert(stockItemUpdate))));
  }

  @Test
  void returns404WhenUpdatedItemIsMissing() throws Exception {
    final var stockItemUpdate =
        org.green.homework.domain.Stock.builder()
            .ID(id)
            .name("A new name!")
            .price(1)
            .quantity(10)
            .build();

    when(stockStoreUsecase.update(stockItemUpdate)).thenThrow(StockNotFoundException.class);

    endpoint
        .perform(
            put("/stock/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new StockUpdate()
                            .name(stockItemUpdate.getName())
                            .price(BigDecimal.valueOf(stockItemUpdate.getPrice()))
                            .quantity(BigDecimal.valueOf(stockItemUpdate.getQuantity())))))
        .andExpect(status().isNotFound());
  }

  @Test
  void returnsSaleInformation() throws JsonProcessingException, Exception {
    final var quantity = 10;
    final var saleRequest = new SaleRequest().quantity(BigDecimal.valueOf(quantity));
    final var sale =
        Sale.builder()
            .ID(UUID.randomUUID())
            .stockID(stockID)
            .price(10)
            .quantity(quantity)
            .time(OffsetDateTime.now())
            .build();

    when(sellStockUsecase.sell(stockID, quantity)).thenReturn(sale);

    endpoint
        .perform(
            post("/stock/{stockID}/sales", stockID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleRequest)))
        .andExpect(status().isCreated())
        .andExpect(
            header().string("Location", "/stock/" + stockID.toString() + "/sales/" + sale.getID()));
  }

  @Test
  void deletesStockById() throws Exception {
    endpoint.perform(delete("/stock/{stockID}", stockID)).andExpect(status().isOk());
  }
}
