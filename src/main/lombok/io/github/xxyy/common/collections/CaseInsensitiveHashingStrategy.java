package io.github.xxyy.common.collections;

import gnu.trove.strategy.HashingStrategy;

/**
 * A case-insensitive hashing strategy for maps with String keys.
 * Hugely inspired by md-5's impl in BungeeCord.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 8.7.14
 */
public class CaseInsensitiveHashingStrategy implements HashingStrategy<String> {
    public static CaseInsensitiveHashingStrategy INSTANCE = new CaseInsensitiveHashingStrategy();

    @Override
    public int computeHashCode(String str) {
        return str.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(String str, String str2) {
        return str.equalsIgnoreCase(str2);
    }
}
