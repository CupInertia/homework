package org.green.homework.domain.usecases;

import java.util.UUID;
import org.green.homework.domain.Sale;

public interface SellStockUsecase {
  Sale sell(UUID stockID, long quantity);
}
