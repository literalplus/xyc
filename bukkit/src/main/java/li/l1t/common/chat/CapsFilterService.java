/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.chat;

/**
 * Provides a service to filter message in all caps (or mostly uppercase, for that matter)
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 04/06/15
 */
public class CapsFilterService {
    private float capsFactor;
    private int ignoreUntilLength;

    /**
     * Constructs a new filter service with a pre-defined {@link #setCapsFactor(float) caps factor} and a
     * {@link #setIgnoreUntilLength(int) ignore length} of 5 characters.
     *
     * @param capsFactor        the fraction of a message which is allowed to be upper case
     * @param ignoreUntilLength the maximum length of a message for it to be ignored by the filter entirely
     */
    public CapsFilterService(float capsFactor, int ignoreUntilLength) {
        this.capsFactor = capsFactor;
        this.ignoreUntilLength = ignoreUntilLength;
    }

    /**
     * Constructs a new filter service with a pre-defined {@link #setCapsFactor(float) caps factor} and a
     * {@link #setIgnoreUntilLength(int) ignore length} of 5 characters.
     *
     * @param capsFactor the fraction of a message which is allowed to be upper case
     */
    public CapsFilterService(float capsFactor) {
        this(capsFactor, 5);
    }

    /**
     * Constructs a mew filter service with a caps factor of 0.5. (50%)
     */
    public CapsFilterService() {
        this(0.5F);
    }

    /**
     * Computes whether a specific message exceeds the capitalisation factor set for this filter service, that
     * means whether more than that fraction of characters are upper case.
     *
     * @param message the message to check
     * @return whether that message exceeds this service's limit
     */
    public boolean check(String message) {
        if (message.length() < ignoreUntilLength) {
            return false;
        }

            //Limit in characters for this specific message
            int specificCapsLimit = (int) (getCapsFactor() * message.length());

        long capsCount = message.chars()
                .filter(Character::isUpperCase)
                .count();

        return capsCount > specificCapsLimit;
    }

    /**
     * Returns the fraction of a message which is allowed to be upper case, between 0 and 1.
     *
     * @return the fraction of a message which is allowed to be upper case
     */
    public float getCapsFactor() {
        return capsFactor;
    }

    /**
     * Sets the fraction of a message which is allowed to be upper case, between 0 and 1
     *
     * @param capsFactor the fraction of a message which is allowed to be upper case
     */
    public void setCapsFactor(float capsFactor) {
        this.capsFactor = capsFactor;
    }

    /**
     * @return the maximum length of a message for it to be ignored by the filter entirely
     */
    public int getIgnoreUntilLength() {
        return ignoreUntilLength;
    }

    /**
     * Sets the amount of characters from which on messages are processed by the filter. Very short messages may be
     * legitimate even if all upper case.
     *
     * @param ignoreUntilLength the maximum length of a message for it to be ignored by the filter entirely
     */
    public void setIgnoreUntilLength(int ignoreUntilLength) {
        this.ignoreUntilLength = ignoreUntilLength;
    }
}
