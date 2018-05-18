package com.chainstaysoftware.immutablevalidator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImmutablesTest {
   @Test
   void get() {
      assertThat(new Immutables().get())
         .as("should contain lines from file")
         .contains("java.io.File");
   }
}