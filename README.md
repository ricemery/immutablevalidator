# Immutable Validator

Simple utility to be used as part of unit testing Java classes to verify immutability.

## Functionality

Used to verify that a Java class and the members contained within the class are final. 

This utility does NOT walk an entire object hierarchy to check that all dependencies 
within the chain are immutable. This utility is only concerned with checking the top
level members of a class.

## Dependencies

No runtime dependencies outside of the JDK.

## Usage - Simple Example

```java
  immutableValidator(ImmutableClass.class)
     .validate();
```

## Usage - Example with contained collections or arrays

The Java collection classes are not final. This utility does not attempt
to determine if a contained collection is immutable. It is up to the
developer to review collection and array handling within a class. And, indicate
to the utility that the collection or array is handled properly.

```java
  immutableValidator(ImmutableClassWithList.class)
     .immutableField("list", "list2")
     .validate();
```

## Usage - Example with contained interfaces/non-final classes

This utility cannot determine if a class member of an interface type is 
immutable. And, it may be necessary to treat some classes as final
that are not marked as final. This utility allows for indicating
when to treat interfaces and classes as final.

```java
  immutableValidator(ImmutableClassWithClass.class)
     .immutableClass(NotFinalClass.class)
     .validate();
```

## Usage - Example with non-final class

This utility will fail when validating a non-final class. The functionality
to fail on a non-final class can be turned off.

```java
  immutableValidator(NotFinalClass.class)
     .allowSubclassing()
     .validate();
```

## Alternatives

* https://github.com/MutabilityDetector/MutabilityDetector
