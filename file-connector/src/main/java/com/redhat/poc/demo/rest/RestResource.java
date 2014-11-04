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
package com.redhat.poc.demo.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

import javax.ws.rs.*;

/**
 * Created by ceposta
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
@Path("/credit")
@Api(value = "/credit", description = "Operations for managing credit info")
public class RestResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/customers/{id}")
    @ApiOperation(value="Put a set of credit statuemts into the customers file", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Invalid credit"),
            @ApiResponse(code = 204, message = "Customer not found")
    })
    public Response putCreditInfo(CreditInfo payload, @PathParam("id") String id) {
        return null;
    }

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value="tests the service", response = Response.class)
    public String getTest(){ return null; }
}
