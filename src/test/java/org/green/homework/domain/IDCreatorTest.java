package org.green.homework.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.UUID;
import org.junit.jupiter.api.Test;

public class IDCreatorTest {
  @Test
  void createsID() {
    final var idCreator = new IDCreator();

    final var id = idCreator.create();

    assertThat(id, is(UUID.fromString(id.toString())));
  }
}
