package com.cyecize.app.util;

import lombok.Getter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class Page<T> implements Iterable<T> {

    private final long pages;

    private final long itemsCount;

    private final Collection<T> elements;

    private final PageQuery pageable;

    public Page(Collection<T> elements, long itemsCount, PageQuery pageable) {
        this.elements = elements;
        this.itemsCount = itemsCount;
        this.pageable = pageable;
        this.pages = (long) Math.ceil(itemsCount * 1.0 / pageable.getSize());
    }

    public Page(Collection<T> elements, PageQuery pageable) {
        this(elements, elements.size(), pageable);
    }

    @Override
    public Iterator<T> iterator() {
        return this.elements.iterator();
    }

    public <E> Page<E> map(Function<T, E> mapFunction) {
        final List<E> mappedElements = this.elements.stream().map(mapFunction).collect(Collectors.toList());
        return new Page<>(mappedElements, this.itemsCount, this.pageable);
    }
}
