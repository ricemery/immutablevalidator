package com.chainstaysoftware.immutablevalidator;


import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static com.chainstaysoftware.immutablevalidator.ImmutableValidator.immutableValidator;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ImmutableValidatorTest {
   @Test
   void validateImmutable() {
      immutableValidator(ImmutableClass.class)
         .validate();
   }

   @Test
   void validateImmutableWithList() {
      immutableValidator(ImmutableClassWithList.class)
         .immutableField("list")
         .validate();
   }

   @Test
   void validateImmutableWithList2() {
      final List<String> assumptions = new LinkedList<>();
      assumptions.add("list");

      immutableValidator(ImmutableClassWithList.class)
         .immutableField(assumptions)
         .validate();
   }

   @Test
   void validateImmutableWithList_DontAssume() {
      assertThatExceptionOfType(NotImmutableException.class)
         .as("Class with list is immutable if no assumptions are made")
         .isThrownBy(() -> immutableValidator(ImmutableClassWithList.class)
            .validate());
   }

   @Test
   void validateNotFinal() {
      assertThatExceptionOfType(NotImmutableException.class)
         .as("Class without final modifier is mutable")
         .isThrownBy(() -> immutableValidator(NotFinalClass.class)
            .validate());
   }

   @Test
   void validateNotFinalALlowSubclassing() {
      immutableValidator(NotFinalClass.class)
         .allowSubclassing()
         .validate();
   }


   @Test
   void validateImmutableWithFile() {
      immutableValidator(ImmutableClassWithFile.class)
         .validate();
   }
}