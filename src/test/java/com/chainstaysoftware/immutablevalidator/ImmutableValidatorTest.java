package com.chainstaysoftware.immutablevalidator;


import com.chainstaysoftware.immutablevalidator.testclasses.ImmutableClass;
import com.chainstaysoftware.immutablevalidator.testclasses.ImmutableClassWithArray;
import com.chainstaysoftware.immutablevalidator.testclasses.ImmutableClassWithClass;
import com.chainstaysoftware.immutablevalidator.testclasses.ImmutableClassWithFile;
import com.chainstaysoftware.immutablevalidator.testclasses.ImmutableClassWithList;
import com.chainstaysoftware.immutablevalidator.testclasses.NotFinalClass;
import com.chainstaysoftware.immutablevalidator.testclasses.NotFinalClass2;
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
         .immutableField("list", "list2")
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
         .as("Class with list is mutable if no assumptions are made")
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
   void validateNotFinalAllowSubclassing() {
      immutableValidator(NotFinalClass.class)
         .allowSubclassing()
         .validate();
   }

   @Test
   void validateImmutableWithFile() {
      immutableValidator(ImmutableClassWithFile.class)
         .validate();
   }

   @Test
   void validateImmutableWithArray() {
      immutableValidator(ImmutableClassWithArray.class)
         .immutableField("foo")
         .validate();
   }

   @Test
   void validateImmutableWithClass() {
      immutableValidator(ImmutableClassWithClass.class)
         .immutableClass(NotFinalClass.class)
         .validate();
   }

   @Test
   void validateImmutableWithClassFail() {
      assertThatExceptionOfType(NotImmutableException.class)
         .as("Class with mutable member is mutable if no assumptions are made")
         .isThrownBy(() -> immutableValidator(ImmutableClassWithClass.class)
            .validate());
   }

   @Test
   void validateImmutableWithMultClass() {
      immutableValidator(ImmutableClassWithClass.class)
         .immutableClass(NotFinalClass.class, NotFinalClass2.class)
         .validate();
   }

   @Test
   void validateImmutableWithMultClass2() {
      final List<Class> assumptions = new LinkedList<>();
      assumptions.add(NotFinalClass.class);
      assumptions.add( NotFinalClass2.class);

      immutableValidator(ImmutableClassWithClass.class)
         .immutableClass(assumptions)
         .validate();
   }
}