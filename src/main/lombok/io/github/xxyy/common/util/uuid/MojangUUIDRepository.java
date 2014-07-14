package io.github.xxyy.common.util.uuid;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import io.github.xxyy.common.lib.com.mojang.api.profiles.HttpProfileRepository;
import io.github.xxyy.common.lib.com.mojang.api.profiles.Profile;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A {@link io.github.xxyy.common.util.uuid.UUIDRepository} backed by the Mojang Name->UUID SOAP API.
 * This impl only supports Name->UUID lookup, reverse lookup can only be provided in a limited manner, by
 * checking cached UUIDs.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 14.7.14
 */
public class MojangUUIDRepository implements UUIDRepository {
    private static final HttpProfileRepository HTTP_PROFILE_REPOSITORY = new HttpProfileRepository("minecraft");
    private final LoadingCache<String, UUID> uuidCache = (LoadingCache<String, UUID>) CacheBuilder.newBuilder() //compilation error "incompatible types" when omitting cast
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .maximumSize(4_200)
            .build(new CacheLoader<String, UUID>() {
                @Override
                @NotNull
                public UUID load(@NotNull String name) throws UnknownKeyException, InvalidResultException {
                    Profile[] profiles = HTTP_PROFILE_REPOSITORY.findProfilesByNames(name);

                    if (profiles.length == 1 && !profiles[0].isDemo()) {
                        return profiles[0].getUniqueId();
                    } else if (profiles.length == 0) {
                        throw new UnknownKeyException();
                    } else {
                        throw new InvalidResultException(profiles);
                    }
                }
            });

    private UUIDRepository parent = EmptyUUIDRepository.INSTANCE;


    @Override
    public UUID forName(String name) {
        try {
            return uuidCache.getUnchecked(name);
        } catch (UncheckedExecutionException e) {
            if (e.getCause() instanceof UnknownKeyException) {
                return parent.forName(name);
            } else {
                throw e;
            }
        }
    }

    @NotNull
    @Override
    public UUID forNameChecked(String name) throws UnknownKeyException, InvalidResultException {
        try {
            return uuidCache.get(name);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof UnknownKeyException) {
                return parent.forNameChecked(name);
            } else if (e.getCause() instanceof InvalidResultException) {
                throw (InvalidResultException) e.getCause();
            } else {
                throw new UncheckedExecutionException(e.getCause());
            }
        }
    }

    @Override
    public String getName(UUID uuid) {
        for (Map.Entry<String, UUID> entry : uuidCache.asMap().entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }

        return parent.getName(uuid);
    }

    @Override
    public UUIDRepository getParent() {
        return parent;
    }

    @Override
    public void setParent(UUIDRepository newParent) {
        parent = newParent;
    }
}
