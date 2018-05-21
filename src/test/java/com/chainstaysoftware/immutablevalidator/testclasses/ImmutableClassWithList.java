package com.chainstaysoftware.immutablevalidator.testclasses;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class ImmutableClassWithList {
   private final List<String> list = ImmutableList.of("foo");
}
