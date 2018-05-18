package com.chainstaysoftware.immutablevalidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

class Immutables {
   private static final String IMMUTABLES = "/com.chainstaysoftware.immutablevalidator/immutables.txt";

   Set<String> get() {
      try (final InputStream inputStream = Immutables.class.getResourceAsStream(IMMUTABLES)) {
         if (inputStream == null)
            throw new IllegalArgumentException(IMMUTABLES + " is not a valid resource");

         final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

         return reader.lines()
            .map(String::trim)
            .filter(line -> !line.startsWith("#"))
            .collect(Collectors.toSet());
      } catch (IOException exception) {
         throw new IllegalStateException("Error loading " + IMMUTABLES);
      }
   }
}
