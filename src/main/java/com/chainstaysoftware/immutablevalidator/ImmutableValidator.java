package com.chainstaysoftware.immutablevalidator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ImmutableValidator {
   private Set<String> immutables = new Immutables().get();
   private Class clazz;
   private Set<String> assumeImmutable = new HashSet<>();
   private boolean allowSubclassing = false;

   private ImmutableValidator() {}

   public static ImmutableValidator immutableValidator(final Class clazz) {
      final ImmutableValidator validator = new ImmutableValidator();
      validator.clazz = clazz;
      return validator;
   }

   public ImmutableValidator immutableClasses(final Class clazz) {
      immutables.add(clazz.getCanonicalName());
      return this;
   }

   public ImmutableValidator immutableClasses(final Class... classes) {
      Arrays.stream(classes).forEach(clazz ->
         immutables.add(clazz.getCanonicalName()));
      return this;
   }

   public ImmutableValidator immutableClasses(final Collection<Class> classes) {
      classes.forEach(clazz ->
         immutables.add(clazz.getCanonicalName()));
      return this;
   }

   public ImmutableValidator immutableFields(final Collection<String> fieldNames) {
      assumeImmutable.addAll(fieldNames);
      return this;
   }

   public ImmutableValidator immutableFields(final String... fieldNames) {
      assumeImmutable.addAll(Arrays.asList(fieldNames));
      return this;
   }

   public ImmutableValidator immutableFields(final String fieldName) {
      assumeImmutable.add(fieldName);
      return this;
   }

   public ImmutableValidator allowSubclassing() {
      this.allowSubclassing = true;
      return this;
   }

   public void validate() {
      if (clazz == null)
         throw new IllegalStateException("Class not set to validate");

      checkFinal(clazz);

      final Field[] declaredFields = clazz.getDeclaredFields();
      for (final Field declaredField :declaredFields) {
         checkFinal(declaredField);
      }
   }

   private void checkFinal(final Field field) {
      if (assumeImmutable.contains(field.getName()))
         return;

      if (field.getName().startsWith("$"))
         return;

      if (field.getType().isArray())
         throw new NotImmutableException(field.getName() + " is any array. Validate that the array is safely copied.");

      if (!Modifier.isFinal(field.getModifiers()))
         throw new NotImmutableException(field.getName() + " is not a final field");

      checkFinal(field.getType());
   }

   private void checkFinal(final Class clz) {
      if (allowSubclassing || immutables.contains(clz.getCanonicalName()))
         return;

      if (!Modifier.isFinal(clz.getModifiers()))
         throw new NotImmutableException(clz.getCanonicalName() + " is not a final class");
   }
}
