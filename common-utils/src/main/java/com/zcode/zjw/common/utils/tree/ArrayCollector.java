package com.zcode.zjw.common.utils.tree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author zhangjiwei
 **/
public class ArrayCollector<R> implements Collector<R, List<R>, R[]> {
    private static final Set<Characteristics> CHARACTERISTICS = Collections.emptySet();
    private final Class<R> elementType;

    /**
     * Constructs a new instance for the given element type.
     *
     * @param elementType The element type.
     */
    public ArrayCollector(final Class<R> elementType) {
        this.elementType = elementType;
    }

    @Override
    public BiConsumer<List<R>, R> accumulator() {
        return List::add;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return CHARACTERISTICS;
    }

    @Override
    public BinaryOperator<List<R>> combiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Function<List<R>, R[]> finisher() {
        return list -> {
            final R[] array = (R[]) Array.newInstance(elementType, list.size());
            return list.toArray(array);
        };
    }

    @Override
    public Supplier<List<R>> supplier() {
        return ArrayList::new;
    }
}
