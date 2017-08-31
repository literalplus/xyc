/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package li.l1t.common.util.config;

import com.google.common.base.Preconditions;
import li.l1t.common.collections.Pair;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * A map config that uses a hash map as backend.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-01-10
 */
public class HashMapConfig implements MapConfig {
    private final Map<String, Object> data;
    private BiConsumer<String, Object[]> typeErrorHandler = (msg, args) -> {
    };

    private HashMapConfig(Map<String, Object> data) {
        Preconditions.checkNotNull(data, "data");
        this.data = data;
    }

    @Override
    public void addTypeErrorHandler(BiConsumer<String, Object[]> typeErrorHandler) {
        Preconditions.checkNotNull(typeErrorHandler, "typeErrorHandler");
        this.typeErrorHandler = this.typeErrorHandler.andThen(typeErrorHandler);
    }

    public static MapConfig of(Map<String, Object> data) {
        return new HashMapConfig(data);
    }

    public static MapConfig ofPairs(Collection<Pair<String, Object>> pairs) {
        Preconditions.checkNotNull(pairs, "pairs");
        return of(pairs.stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));
    }

    public static MapConfig copyOf(Map<String, Object> data) {
        return of(new HashMap<>(data));
    }

    @Override
    public Map<String, Object> raw() {
        return data;
    }

    @Override
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    @Override
    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public Optional<Object> find(String key) {
        return Optional.ofNullable(get(key));
    }

    @Override
    public <T> Optional<T> findTyped(String key, Class<T> type) {
        return find(key)
                .filter(typeFilter(type, key))
                .map(type::cast);
    }

    private Predicate<Object> typeFilter(Class<?> expectedType, String key) {
        return obj -> {
            if (obj != null && expectedType.isAssignableFrom(obj.getClass())) {
                return true;
            } else {
                typeErrorHandler.accept(
                        "Unexpected type for {} - expected instance of {}, but got {}",
                        new Object[]{key, expectedType, obj}
                );
                return false;
            }
        };
    }

    private Predicate<? super Map.Entry<?, ?>> entryTypeFilter(Class<?> keyType, Class<?> valueType, String key) {
        return entry ->
                typeFilter(keyType, key).test(entry.getKey()) &&
                        typeFilter(valueType, key).test(entry.getValue());
    }

    @Override
    public Optional<String> findString(String key) {
        return findTyped(key, String.class);
    }

    @Override
    public Optional<String> stringify(String key) {
        return find(key).map(String::valueOf);
    }

    @Override
    public <V, R extends Collection<V>> R getCollection(String key, Class<V> valueType, Collector<V, ?, R> collector) {
        Collection<?> objCollection = findTyped(key, Collection.class).orElseGet(Collections::emptyList);
        return objCollection.stream()
                .filter(typeFilter(valueType, key + "[]"))
                .map(valueType::cast)
                .collect(collector);
    }

    @Override
    public <K, V> Map<K, V> getMap(String key, Class<K> keyType, Class<V> valueType) {
        Map<?, ?> objMap = findTyped(key, Map.class).orElseGet(HashMap::new);
        return objMap.entrySet().stream()
                .filter(entryTypeFilter(keyType, valueType, key + "<>"))
                .map(e -> Pair.pairOf(keyType.cast(e.getKey()), valueType.cast(e.getValue())))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }
}
