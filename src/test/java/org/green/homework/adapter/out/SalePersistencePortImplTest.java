package org.green.homework.adapter.out;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.green.homework.domain.Sale;
import org.green.homework.domain.port.out.SalePersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
public class SalePersistencePortImplTest {

  @Autowired private SalePersistencePort salePersistencePort;

  @Test
  void persistsSale() {
    final var id = UUID.randomUUID();
    final var newSale =
        Sale.builder()
            .ID(id)
            .stockID(UUID.randomUUID())
            .price(1.0)
            .quantity(5)
            .time(OffsetDateTime.now())
            .build();

    final var sale = salePersistencePort.save(newSale);

    assertThat(sale, is(newSale));
    assertThat(sale, is(salePersistencePort.getById(id)));
  }
}
