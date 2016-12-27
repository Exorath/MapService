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

package com.exorath.service.map.res;

/**
 * Created by toonsev on 12/27/2016.
 */
public class DownloadMapReq {
    private String userId;
    private String mapId;
    private String envId;
    private String versionId;
    public DownloadMapReq(){}

    public DownloadMapReq(String userId, String mapId, String envId, String versionId) {
        this.userId = userId;
        this.mapId = mapId;
        this.envId = envId;
        this.versionId = versionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMapId() {
        return mapId;
    }

    public String getEnvId() {
        return envId;
    }

    public String getVersionId() {
        return versionId;
    }
}
