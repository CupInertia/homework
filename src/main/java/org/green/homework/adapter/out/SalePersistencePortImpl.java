package org.green.homework.adapter.out;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.green.homework.domain.Sale;
import org.green.homework.domain.port.out.SalePersistencePort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalePersistencePortImpl implements SalePersistencePort {

  private final SaleRepository saleRepository;

  @Override
  public Sale save(Sale sale) {
    return convert(saleRepository.save(convert(sale)));
  }

  @Override
  public Sale getById(UUID ID) {
    return convert(saleRepository.findById(ID.toString()));
  }

  private static Sale convert(SaleEntity source) {
    return Sale.builder()
        .ID(UUID.fromString(source.getId()))
        .stockID(UUID.fromString(source.getStockId()))
        .price(source.getPrice())
        .quantity(source.getQuantity())
        .time(source.getTime())
        .build();
  }

  private static SaleEntity convert(Sale sale) {
    final var saleEntity = new SaleEntity();
    saleEntity.setId(sale.getID().toString());
    saleEntity.setStockId(sale.getStockID().toString());
    saleEntity.setPrice(sale.getPrice());
    saleEntity.setQuantity(sale.getQuantity());
    saleEntity.setTime(sale.getTime());

    return saleEntity;
  }
}
