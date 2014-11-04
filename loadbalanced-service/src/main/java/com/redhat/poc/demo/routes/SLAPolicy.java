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

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.management.event.*;
import org.apache.camel.support.EventNotifierSupport;
import org.apache.camel.util.EndpointHelper;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.EventObject;
import java.util.List;

/**
 * Created by ceposta
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
public class SLAPolicy extends EventNotifierSupport {

    private static final List<String> DEFAULT_INCLUDES = Collections.EMPTY_LIST;
    private static final List<String> DEFAULT_EXCLUDES = Collections.EMPTY_LIST;

    private static final String BREADCRUMB_ID_HEADER = "breadcrumbId";

    @EndpointInject(uri = "seda:sendEmail")
    protected ProducerTemplate emailService;


    // default threshold in ms
    private static final long DEFAULT_THRESHOLD = 10000;


    private List<String> includes;
    private List<String> excludes;

    private long thresHoldMs = DEFAULT_THRESHOLD;

    public SLAPolicy() {
        setIgnoreCamelContextEvents(true);
        setIgnoreServiceEvents(true);
        this.includes = (includes != null) ? includes : DEFAULT_INCLUDES;
        this.excludes = (excludes != null) ? excludes : DEFAULT_EXCLUDES;
        this.log = LoggerFactory.getLogger(SLAPolicy.class);
    }

    @Override
    public void notify(EventObject event) throws Exception {

        String breadcrumbId = null;
        if (event instanceof AbstractExchangeEvent) {
            breadcrumbId = ((AbstractExchangeEvent) event).getExchange().getIn().getHeader(BREADCRUMB_ID_HEADER, String.class);
        }

        if (event instanceof ExchangeSentEvent) {
            ExchangeSentEvent sentEvent = ((ExchangeSentEvent) event);
            long timeToSend = sentEvent.getTimeTaken();

            log.info(String.format("Message {breadcrumbId: '%s' took '%sms' to send to '%s'}", breadcrumbId, timeToSend, sentEvent.getEndpoint()));

            if (timeToSend > 10000) {
                String message = String.format("Took too long (> %sms) to send to endpoint: %s", thresHoldMs, sentEvent.getEndpoint());
                log.warn(message);
                emailService.sendBodyAndHeader(message, "subject", "SLA Exceeded");
            }

        }

    }

    private boolean endpointOfInterest(Endpoint endpoint) {
        boolean ofInterest = false;
        for (String include : includes) {
            ofInterest |= EndpointHelper.matchEndpoint(endpoint.getCamelContext(), endpoint.getEndpointUri(), include);
            if (ofInterest) {
                break;
            }
        }
        for (String exclude : excludes) {
            ofInterest &= !EndpointHelper.matchEndpoint(endpoint.getCamelContext(), endpoint.getEndpointUri(), exclude);
            if (!ofInterest) {
                break;
            }
        }
        return ofInterest;
    }

    @Override
    public boolean isEnabled(EventObject event) {
        return ((event instanceof ExchangeSendingEvent
                && endpointOfInterest(((ExchangeSendingEvent) event).getEndpoint()))
                || (event instanceof ExchangeSentEvent
                && endpointOfInterest(((ExchangeSentEvent) event).getEndpoint()))
                || event instanceof ExchangeCreatedEvent
                || event instanceof ExchangeCompletedEvent);
    }

    public long getThresHoldMs() {
        return thresHoldMs;
    }

    public void setThresHoldMs(long thresHoldMs) {
        this.thresHoldMs = thresHoldMs;
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = (includes != null) ? includes : DEFAULT_INCLUDES;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = (excludes != null) ? excludes : DEFAULT_EXCLUDES;
    }
}
