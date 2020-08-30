package dev.demo.java8;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
