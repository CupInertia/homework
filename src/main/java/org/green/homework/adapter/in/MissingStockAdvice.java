package org.green.homework.adapter.in;

import org.green.homework.domain.LackingSupplyException;
import org.green.homework.domain.StockNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MissingStockAdvice {

  @ExceptionHandler(StockNotFoundException.class)
  protected ResponseEntity<Object> handleMissingStock() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(LackingSupplyException.class)
  protected ResponseEntity<Object> handleLackingSupply(LackingSupplyException exception) {
    return ResponseEntity.of(
            ProblemDetail.forStatusAndDetail(HttpStatus.OK, exception.getMessage()))
        .build();
  }
}
