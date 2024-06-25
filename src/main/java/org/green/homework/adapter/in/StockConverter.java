package org.green.homework.adapter.in;

import java.math.BigDecimal;
import org.green.homework.domain.Stock;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StockConverter implements Converter<Stock, org.openapitools.model.Stock> {

  @Override
  public org.openapitools.model.Stock convert(Stock source) {
    return new org.openapitools.model.Stock()
        .id(source.getID().toString())
        .name(source.getName())
        .price(BigDecimal.valueOf(source.getPrice()))
        .quantity(BigDecimal.valueOf(source.getQuantity()));
  }
}
