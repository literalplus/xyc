package io.github.xxyy.common.checklist.renderer;

import java.util.function.Function;

/**
 * Builds checkmark-based renderers.
 *
 * @param <T> Product of the builder
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.8.14
 */
public abstract class CheckmarkBasedRendererBuilder<T extends CheckmarkBasedRenderer> {
    /**
     * Renders UTF-8 checkmarks surrounded by brackets. Unchecked items will result in a X mark.
     */
    protected static final Function<Boolean, String> BRACKET_CHECKMARK_RENDERER = (ckd) -> ckd ? "[✓]" : "[✗]";
    /**
     * Renders UTF-8 checkmarks surrounded by brackets. Unchecked items will result in an empty mark.
     */
    protected static final Function<Boolean, String> BRACKET_EMPTY_CHECKMARK_RENDERER = (ckd) -> ckd ? "[✓]" : "[ ]";
    /**
     * Renders simple UTF-8 checkmarks, not surrounded by brackets. Unchecked items will result in a X mark.
     */
    protected static final Function<Boolean, String> CHECKMARK_RENDERER = (ckd) -> ckd ? "✓" : "✗";
    /**
     * Renders simple UTF-8 checkmarks, not surrounded by brackets. Unchecked items will result in a space.
     */
    protected static final Function<Boolean, String> EMPTY_CHECKMARK_RENDERER = (ckd) -> ckd ? "✓" : " ";

    private boolean uncheckedEmpty = false; //When changing these defaults, make sure to also change the values in
    private boolean brackets = true;        // #customCheckmarks(...)!
    private Function<Boolean, String> customRenderer;

    /**
     * Sets the "unchecked empty" value for this builder.
     *
     * @param uncheckedEmpty whether unchecked items will be not checked using a space instead of an X mark.
     * @return this builder
     */
    public CheckmarkBasedRendererBuilder<T> uncheckedEmpty(boolean uncheckedEmpty) {
        validateNoCustomMarks();
        this.uncheckedEmpty = uncheckedEmpty;
        return this;
    }

    /**
     * Sets the "unchecked empty" value for this builder.
     *
     * @param uncheckedEmpty whether unchecked items will be not checked using a space instead of an X mark.
     * @return this builder
     * @see #uncheckedEmpty(boolean)
     * @deprecated Typo in method name. Use the correctly-spelled version.
     */
    @Deprecated
    public CheckmarkBasedRendererBuilder<T> umcheckedEmpty(boolean uncheckedEmpty) {
        return uncheckedEmpty(uncheckedEmpty);
    }

    /**
     * Sets whether the resulting renderer will use brackets [] to wrap checkmarks.
     *
     * @param brackets whether checkmarks will be wrapped in brackets.
     * @return this builder
     */
    public CheckmarkBasedRendererBuilder<T> brackets(boolean brackets) {
        validateNoCustomMarks();
        this.brackets = brackets;
        return this;
    }

    /**
     * Sets a custom checkmark factory for this builder.
     *
     * @param checkmarkRenderer the custom checkmark builder to set
     * @return this builder
     */
    public CheckmarkBasedRendererBuilder<T> customCheckmarks(Function<Boolean, String> checkmarkRenderer) {
        if (uncheckedEmpty || !brackets) { //Not matching defaults -> definitely changed
            throw new IllegalStateException("Cannot set custom checkmark renderer when default renderer properties are set!");
        }
        this.customRenderer = checkmarkRenderer;
        return this;
    }

    /**
     * Builds an object using this builder's parameters.
     *
     * @return the resulting object
     */
    public T build() {
        Function<Boolean, String> finalRenderer;
        if (customRenderer != null) {
            finalRenderer = customRenderer;
        } else {
            finalRenderer = getCheckmarkRenderer(brackets, uncheckedEmpty);
        }

        return getInstance(finalRenderer);
    }

    /**
     * Gets an instance of the built type.
     *
     * @param renderer the renderer the returned instance will use
     * @return an instance of the built type corresponding to the argument
     */
    protected abstract T getInstance(Function<Boolean, String> renderer);

    private void validateNoCustomMarks() {
        if (customRenderer != null) {
            throw new IllegalStateException("Cannot set default checkmark property when a custom renderer is set!");
        }
    }

    public static Function<Boolean, String> getCheckmarkRenderer(boolean brackets, boolean uncheckedEmpty) {
        if (brackets) {
            return uncheckedEmpty ? BRACKET_EMPTY_CHECKMARK_RENDERER : BRACKET_CHECKMARK_RENDERER;
        } else {
            return uncheckedEmpty ? EMPTY_CHECKMARK_RENDERER : CHECKMARK_RENDERER;
        }
    }
}
