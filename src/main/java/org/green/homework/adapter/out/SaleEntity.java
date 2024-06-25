package org.green.homework.adapter.out;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SALES")
@NoArgsConstructor
@Getter
@Setter
public class SaleEntity {
  @Id private String id;

  @NotNull private String stockId;

  @NotNull private long quantity;

  @NotNull private double price;

  @NotNull private OffsetDateTime time;
}
