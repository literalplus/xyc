package io.github.xxyy.common.collections;

import gnu.trove.map.hash.TCustomHashMap;

import java.util.Map;

/**
 * A HashMap which simply ignores key casing.
 * Hugely inspired by md-5's impl in BungeeCord.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 8.7.14
 */
public class CaseInsensitiveMap<V> extends TCustomHashMap<String, V> {

    public CaseInsensitiveMap() {
        super(CaseInsensitiveHashingStrategy.INSTANCE);
    }

    public CaseInsensitiveMap(Map<String, V> map) {
        super(CaseInsensitiveHashingStrategy.INSTANCE, map);
    }

    public CaseInsensitiveMap(int initialCapacity) {
        super(CaseInsensitiveHashingStrategy.INSTANCE, initialCapacity);
    }

    public CaseInsensitiveMap(int initialCapacity, float loadFactor) {
        super(CaseInsensitiveHashingStrategy.INSTANCE, initialCapacity, loadFactor);
    }
}
