package org.green.homework.adapter.out;

import org.springframework.data.repository.Repository;

public interface SaleRepository extends Repository<SaleEntity, String> {
  SaleEntity save(SaleEntity sale);

  SaleEntity findById(String ID);
}
