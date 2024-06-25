package org.green.homework.domain.port.out;

import java.util.UUID;
import org.green.homework.domain.Sale;

public interface SalePersistencePort {
  Sale save(Sale sale);

  Sale getById(UUID ID);
}
