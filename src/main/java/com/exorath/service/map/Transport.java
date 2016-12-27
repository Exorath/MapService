/*
 * Copyright 2016 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.service.map;

import com.exorath.service.commons.portProvider.PortProvider;
import com.exorath.service.map.res.GetMapsReq;
import com.google.gson.Gson;
import spark.Route;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Created by toonsev on 11/26/2016.
 */
public class Transport {
    private static final Gson GSON = new Gson();

    public static void setup(Service service, PortProvider portProvider) {
        port(portProvider.getPort());

        get("/accounts/:userId/maps", getGetMapsRoute(service), GSON::toJson);
        get("/accounts/:userId/maps", getGetMapsRoute(service), GSON::toJson);
    }

    public static Route getGetMapsRoute(Service service) {
        return (req, res) -> {
            Integer maxEnvs = req.queryParams().contains("maxEnvs") ? Integer.valueOf(req.queryParams("maxEnvs")) : null;
            GetMapsReq getMapsReq = new GetMapsReq(req.params("userId"), req.queryParams("mapPrefix"), req.queryParams("startAfter"), maxEnvs);
            return service.getMaps(getMapsReq);
        };
    }
}
