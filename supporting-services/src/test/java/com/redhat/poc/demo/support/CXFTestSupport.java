/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.poc.demo.support;

import org.apache.camel.test.AvailablePortFinder;

/**
 * For test cases that use unique contexts, they can share the 
 * ports which will make things a bit faster as ports aren't opened
 * and closed all the time. 
 */
public final class CXFTestSupport {


    private CXFTestSupport() {
    }

    public static int getPort(){
        return AvailablePortFinder.getNextAvailable();
    }
    
    public static int getPort(String name) {
        int port = getPort();
        System.setProperty(name, Integer.toString(port));
        return port;
    }

    public static String getWebServiceUrl(String propName, String host, String contextPath) {
        int port = getPort();
        String url = String.format("http://%s:%s/%s", host, port, contextPath);
        System.setProperty(propName, url);
        return url;
    }
    
}
