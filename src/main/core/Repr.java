package main.core;

public interface Repr<T extends Repr<T>> {
    public String toRepr();
    public T fromRepr(String repr);
}
