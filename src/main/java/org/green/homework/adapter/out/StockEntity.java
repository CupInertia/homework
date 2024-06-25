package org.green.homework.adapter.out;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "STOCK")
@NoArgsConstructor
@Setter
@Getter
public class StockEntity {
  @Id private String ID;

  @NotNull private String name;

  @NotNull private double price;

  @NotNull private long quantity;
}
