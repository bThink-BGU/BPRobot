package il.ac.bgu.cs.bp.bprobot.util;

@FunctionalInterface
public interface TriSupplier<T, A, B, C> {
  T get(A a, B b, C c);
}