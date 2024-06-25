package org.green.homework.domain;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class IDCreator {
  public UUID create() {
    return UUID.randomUUID();
  }
}
