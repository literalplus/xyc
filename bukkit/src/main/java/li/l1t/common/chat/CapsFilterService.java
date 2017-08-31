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
