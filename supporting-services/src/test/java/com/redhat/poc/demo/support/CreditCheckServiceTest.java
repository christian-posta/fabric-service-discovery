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

import org.apache.camel.component.cxf.CxfSpringEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.example.wspcreditcheckservice.RequestType;
import org.example.wspcreditcheckservice.ResponseType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;import java.lang.Exception;import java.lang.Override;import java.lang.String;

/**
 * @author <a href="http://www.christianposta.com/blog">Christian Posta</a>
 */
public class CreditCheckServiceTest extends CamelSpringTestSupport {
    private static Logger LOG = LoggerFactory.getLogger(CreditCheckServiceTest.class);
    private String url;


    @Test
    public void testCallCreditCheckServiceTest() {
        assertCxfEndpointAddress();

        RequestType request = new RequestType();
        request.setName("Christian");
        request.setAddress("Phoenix, AZ");
        request.setSSN("123456789");
        ResponseType response = template.requestBody("cxf:bean:creditCheckCXFService", request, ResponseType.class);
        assertEquals("750", response.getStatus());
    }

    @Test
    public void testSendBackException() {
        assertCxfEndpointAddress();

        RequestType request = new RequestType();
        request.setName("jstrachan");
        request.setAddress("Foo, CO");
        request.setSSN("000000000");
        ResponseType response = template.requestBody("cxf:bean:creditCheckCXFService", request, ResponseType.class);
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/CreditCheckServiceTest-context.xml");

    }

    private void assertCxfEndpointAddress() {
        CxfSpringEndpoint endpoint = (CxfSpringEndpoint) applicationContext.getBean("creditCheckCXFService");
        assertEquals("Incorrect address!", url, endpoint.getAddress());
    }

    @Override
    public void doPreSetup() throws Exception {
        url = CXFTestSupport.getWebServiceUrl("service.creditCheck.publish.url", "localhost", "creditCheck");
        LOG.info("using URL for web service: " + url);
        super.doPreSetup();
    }
}
