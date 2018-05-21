package com.chainstaysoftware.immutablevalidator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to validate if Java class is immutable. Note, that only the class under
 * test is checked, and the class' member variables are checked. The entire
 * class hierarchy is NOT validated.
 */
public class ImmutableValidator {
   private Set<String> immutables = new Immutables().get();
   private Class clazz;
   private Set<String> assumeImmutable = new HashSet<>();
   private boolean allowSubclassing = false;

   private ImmutableValidator() {}

   /**
    * Creates a {@link ImmutableValidator} instance for the passed in
    * {@link Class}
    * @param clazz {@link Class} to test
    * @return {@link ImmutableValidator} instance.
    */
   public static ImmutableValidator immutableValidator(final Class clazz) {
      final ImmutableValidator validator = new ImmutableValidator();
      validator.clazz = clazz;
      return validator;
   }

   /**
    * Informs {@link ImmutableValidator} to assume {@link Class} is
    * immutable.
    * @param clazz {@link Class} to assume is immutable.
    * @return {@link ImmutableValidator} instance.
    */
   public ImmutableValidator immutableClasses(final Class clazz) {
      immutables.add(clazz.getCanonicalName());
      return this;
   }

   /**
    * Informs {@link ImmutableValidator} to assume array of {@link Class} that
    * are immutable.
    * @param classes Array of {@link Class} to assume immutable.
    * @return {@link ImmutableValidator} instance.
    */
   public ImmutableValidator immutableClasses(final Class... classes) {
      Arrays.stream(classes).forEach(clazz ->
         immutables.add(clazz.getCanonicalName()));
      return this;
   }

   /**
    * Informs {@link ImmutableValidator} to assume collection of {@link Class} that
    * are immutable.
    * @param classes Collection of {@link Class} to assume immutable.
    * @return {@link ImmutableValidator} instance.
    */
   public ImmutableValidator immutableClasses(final Collection<Class> classes) {
      classes.forEach(clazz ->
         immutables.add(clazz.getCanonicalName()));
      return this;
   }

   /**
    * Informs {@link ImmutableValidator} to assume collection of fields that are
    * immutable.
    * @param fieldNames Collection of fieldNames assume immutable.
    * @return {@link ImmutableValidator} instance.
    */
   public ImmutableValidator immutableFields(final Collection<String> fieldNames) {
      assumeImmutable.addAll(fieldNames);
      return this;
   }

   /**
    * Informs {@link ImmutableValidator} to assume array of fields that are
    * immutable.
    * @param fieldNames Array of fieldNames assume immutable.
    * @return {@link ImmutableValidator} instance.
    */
   public ImmutableValidator immutableFields(final String... fieldNames) {
      assumeImmutable.addAll(Arrays.asList(fieldNames));
      return this;
   }

   /**
    * Informs {@link ImmutableValidator} to assume field that is
    * immutable.
    * @param fieldName fieldName to assume immutable.
    * @return {@link ImmutableValidator} instance.
    */
   public ImmutableValidator immutableFields(final String fieldName) {
      assumeImmutable.add(fieldName);
      return this;
   }

   /**
    * Informs {@link ImmutableValidator} to allow for the class under test
    * to NOT be final.
    * @return {@link ImmutableValidator} instance.
    */
   public ImmutableValidator allowSubclassing() {
      this.allowSubclassing = true;
      return this;
   }

   /**
    * Performs validation.
    * @throws NotImmutableException if the code detects that the class
    * under test is mutable.
    */
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
