package com.chainstaysoftware.immutablevalidator;

public class NotImmutableException extends RuntimeException {
   public NotImmutableException(String message) {
      super(message);
   }
}
