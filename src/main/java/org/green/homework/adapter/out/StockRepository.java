package org.green.homework.adapter.out;

import org.springframework.data.repository.Repository;

public interface StockRepository extends Repository<StockEntity, String> {
  StockEntity save(StockEntity stock);

  void deleteByID(String ID);

  StockEntity findByID(String ID);
}
