package fr.poulpogaz.jam.renderer.utils;

import java.util.Iterator;

public class ArrayIterator<E> implements Iterator<E> {

    private final E[] objects;
    private int pos = 0;

    public ArrayIterator(E[] objects) {
        this.objects = objects;
    }

    @Override
    public boolean hasNext() {
        return pos < objects.length;
    }

    @Override
    public E next() {
        return objects[pos++];
    }
}