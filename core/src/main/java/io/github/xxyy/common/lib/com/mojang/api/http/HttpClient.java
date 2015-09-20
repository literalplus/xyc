/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.lib.com.mojang.api.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public interface HttpClient {
    String post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException;

    String post(URL url, Proxy proxy, HttpBody body, List<HttpHeader> headers) throws IOException;

    String get(URL url, List<HttpHeader> headers) throws IOException;

    String get(URL url, Proxy proxy, List<HttpHeader> headers) throws IOException;
}
