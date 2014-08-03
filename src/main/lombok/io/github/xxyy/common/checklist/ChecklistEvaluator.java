package io.github.xxyy.common.checklist;

/**
 * An evaluation strategy for {@link Checklist.Item}s, which actually evaluates whether checklist items are done already.
 * This is a functional interface, so feel free to lambda.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.8.14
 */
@FunctionalInterface
public interface ChecklistEvaluator {
    /**
     * This evaluates this item's condition. Note that every call will evaluate it anew and results might change in any matter.
     * @return whether this checklist item will be displayed as checked
     */
    boolean isChecked();
}
