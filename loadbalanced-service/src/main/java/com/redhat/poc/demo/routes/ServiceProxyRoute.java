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
package com.redhat.poc.demo.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by ceposta
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
public class ServiceProxyRoute extends RouteBuilder {

    private String fabricPrefix;

    @Override
    public void configure() throws Exception {
        from(fabric("cxf:bean:creditCheckCXFServiceProxy")).routeId("proxyRoute")
                .onException(Exception.class)
                    .handled(true)
                    .maximumRedeliveries(2)                     // only two (2) retries
                    .redeliveryDelay(5000)                      // retry after five (5) seconds
                    .retryAttemptedLogLevel(LoggingLevel.INFO)  // so we see retry attempts logged
                    .log(LoggingLevel.WARN, ">>>>> Body of failure: body=${body}")
                    .setBody().simple("${exception}")
                    .setHeader("subject", constant("Error sending"))
                    .to("seda:sendEmail")
                    .log("Ending with a failure!")
                .end()
                .removeHeaders("CamelHttp*")
                .log("proxying a service call cluster")
                .to("cxf:bean:creditCheckCXFService")
                .log("End processing proxy route");


        from("seda:sendEmail").routeId("emailRoute")
                .log("Send email!")
                // default characteristics, can override with headers
                .to("smtps://ceposta@smtp.webfaction.com:465?password=throwaway&from=camel@christianposta.com" +
                        "&to=christian@redhat.com");
    }

    public String fabric(String endpoint) {
        return fabricPrefix == null ? endpoint : fabricPrefix + endpoint;
    }

    public String getFabricPrefix() {
        return fabricPrefix;
    }

    public void setFabricPrefix(String fabricPrefix) {
        this.fabricPrefix = fabricPrefix;
    }
}
