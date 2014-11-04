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

import com.redhat.poc.demo.rest.Response;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * Created by ceposta
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
public class FileServiceRoute extends RouteBuilder{


    public void configure() throws Exception {
        from("cxfrs:bean:restServer?bindingStyle=SimpleConsumer").routeId("fileServiceRoute")
                .log("Just got a message for the file service...")
                .marshal().json(JsonLibrary.Jackson)
                .to("file:target/out?fileName=serivce-call-${date:now:yyyyMMdd}")
                .setBody().constant(new Response("success"))
        ;
    }

}
