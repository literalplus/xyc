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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides a service to filter advertisement-like messages from text, for example Minecraft chat. Multiple settings
 * are offered to tune how heuristically the algorithm behaves.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>, Janmm14
 * @since 03/06/15
 */
public class AdFilterService {
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    private static final Pattern HIDDEN_DOTS_PATTERN = Pattern.compile("[(\\[ ]{1,2}((?:punkt|dot|\\.))[)\\] ]{1,2}|\\.{2,}", Pattern.CASE_INSENSITIVE);

    /*
        Matches any URL with(out) http(s) and at least second-level domain part. Paths and query string are also captured.
        The first (and only) capturing group is the second-level domain part of the URL.
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "(?:https?://)?(?:[-\\w_.]{2,}\\.)?([-\\w_]{2,}\\.[a-z]{2,4})(?:/\\S*)?",
            Pattern.CASE_INSENSITIVE); //Don't remove the query part since stuff like "index.php" is detected as URL then

    private final List<String> ignoredDomains = new ArrayList<>();
    private boolean findHiddenDots = true;
    private boolean findIpAddresses = true;

    /**
     * Processes an input message and checks whether it is considered an advertisement with this filter service's
     * settings and corresponding algorithms.
     *
     * @param message the input message to process
     * @return whether this filter service recognises given message as advertisement
     */
    public boolean test(String message) {
        if (findHiddenDots) {
            message = HIDDEN_DOTS_PATTERN.matcher(message).replaceAll(".");
        }

        if (findIpAddresses && IP_PATTERN.matcher(message).find()) {
            return true;
        }

        Matcher matcher = URL_PATTERN.matcher(message); //That pattern is considered pretty accurate, so no setting here
        while (matcher.find()) {
            /*
            ArrayList#contains() loops too and is case-sensitive, so this doesn't make much of a difference while
            providing the benefit of being case-insensitive!
             */
            if (ignoredDomains.stream().noneMatch(s -> matcher.group(1).equalsIgnoreCase(s))) {
                return true;
            }
        }

        return false; //Looks clean for now, but I'm watching you!
    }


    /**
     * Returns whether this service will attempt to find obfuscated dots when checking messages, for example ones
     * enclosed in parentheses or surrounded by spaces. Default value is true.
     *
     * @return whether this service attempts to find obfuscated dots when checking messages
     */
    public boolean isFindHiddenDots() {
        return findHiddenDots;
    }

    /**
     * Sets whether this service will attempt to find obfuscated dots when checking messages, for example ones
     * enclosed in parentheses or surrounded by spaces. Default value is true.
     *
     * @param findHiddenDots whether to attempt to find obfuscated dots when checking messages
     * @return this filter service, for cleaner construction
     */
    public AdFilterService setFindHiddenDots(boolean findHiddenDots) {
        this.findHiddenDots = findHiddenDots;
        return this;
    }

    /**
     * Returns whether this filter service will attempt to find Internet Protocol addresses in messages.
     * Default value is true.
     *
     * @return whether this service filters IP addresses
     */
    public boolean isFindIpAddresses() {
        return findIpAddresses;
    }

    /**
     * Sets whether this filter service will attempt to find Internet Protocol addresses in messages.
     * Default value is true.
     *
     * @param findIpAddresses whether to filter IP addresses
     * @return this filter service, for cleaner construction
     */
    public AdFilterService setFindIpAddresses(boolean findIpAddresses) {
        this.findIpAddresses = findIpAddresses;
        return this;
    }

    /**
     * Returns a mutable list of domain names this filter service ignores when URLs are encountered. Domain names listed
     * here start at second-level and also include the top-level domain. Some valid entries are:
     * <ul>
     * <li>example.com</li>
     * <li>google.com</li>
     * <li>minotopia.me</li>
     * <li>some-doma.in</li>
     * </ul>
     * Subdomains of listed domains will also be ignored.
     *
     * @return a list of second-level domain names which are ignored
     */
    public List<String> getIgnoredDomains() {
        return ignoredDomains;
    }

    /**
     * Convenience method to easily add ignored domains in chained (construction) calls.
     *
     * @param ignored an array of domains as specified by {@link #getIgnoredDomains()}
     * @return this filter service, for cleaner construction
     */
    public AdFilterService addIgnoredDomains(List<String> ignored) {
        ignoredDomains.addAll(
                ignored.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList())
        );
        return this;
    }

    /**
     * Convenience method to easily add ignored domains in chained (construction) calls.
     *
     * @param ignored an array of domains as specified by {@link #getIgnoredDomains()}
     * @return this filter service, for cleaner construction
     */
    public AdFilterService addIgnoredDomains(String... ignored) {
        ignoredDomains.addAll(
                Arrays.stream(ignored)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList())
        );
        return this;
    }
}
