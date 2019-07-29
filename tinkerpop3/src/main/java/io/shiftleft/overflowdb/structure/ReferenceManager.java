package io.shiftleft.overflowdb.structure;

public interface ReferenceManager {
  void registerRef(ElementRef ref);

  void applyBackpressureMaybe();

  void close();

  void clearAllReferences();
}