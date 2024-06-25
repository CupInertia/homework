package org.green.homework.adapter.in;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.UUID;
import org.green.homework.domain.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StockConverter.class)
public class StockConverterTest {

  @Autowired StockConverter converter;

  @Test
  void copiesAllFields() {
    final var id = UUID.randomUUID();
    final var name = "A name!";
    final var price = 10.0;
    final var quantity = 100;
    final var source = Stock.builder().ID(id).name(name).price(price).quantity(quantity).build();

    final var result = converter.convert(source);

    final var expected =
        new org.openapitools.model.Stock()
            .id(id.toString())
            .name(name)
            .price(BigDecimal.valueOf(price))
            .quantity(BigDecimal.valueOf(quantity));

    assertThat(result, is(expected));
  }
}
