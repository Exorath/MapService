/*
 * Copyright 2017 Exorath
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

package com.exorath.service.map.api;

import com.exorath.service.map.res.*;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by toonsev on 1/26/2017.
 */
public class MapServiceAPI {
    private static final Gson GSON = new Gson();
    private String address;

    public MapServiceAPI(String address) {
        this.address = address;
    }

    public InputStream downloadMap(DownloadMapReq downloadMapReq) throws Exception {
        HttpRequest request = Unirest.get(url("/accounts/{userId}/maps/{mapId}/env/{envId}/download"))
                .routeParam("userId", downloadMapReq.getUserId())
                .routeParam("mapId", downloadMapReq.getMapId())
                .routeParam("envId", downloadMapReq.getEnvId());
        if(downloadMapReq.getVersionId() != null)
            request = request.queryString("versionId", downloadMapReq.getVersionId());
        return request.asBinary().getBody();
    }

    public void downloadMapToFolder(DownloadMapReq downloadMapReq, File mapDir) throws Exception{
        if (!mapDir.exists())
            mapDir.mkdirs();
        if (!mapDir.isDirectory())
            throw new IllegalStateException("Mapsdir is not a directory.");
        try(InputStream inputStream = downloadMap(downloadMapReq)){
            if(inputStream == null)
                throw new NullPointerException("Failed to download a map.");
            unZip(inputStream, mapDir);
        }
    }

    public GetMapsRes getMaps(GetMapsReq getMapsReq) throws Exception{
        HttpRequest request = Unirest.get(url("/accounts/{userId}/maps"))
                .routeParam("userId", getMapsReq.getUserId());
        if(getMapsReq.getMaxEnvs() != null)
            request = request.queryString("maxEnvs", getMapsReq.getMaxEnvs());
        if(getMapsReq.getStartAfter() != null)
            request = request.queryString("startAfter", getMapsReq.getStartAfter());
        String body = request.asString().getBody();
        GetMapsRes getMapsRes = GSON.fromJson(body, GetMapsRes.class);
        return getMapsRes;
    }

    public GetMapEnvRes getMapEnv(GetMapEnvReq getMapEnvReq) throws Exception {
        HttpRequest request = Unirest.get(url("/accounts/{userId}/maps/{mapId}/env/{envId}"))
                .routeParam("userId", getMapEnvReq.getUserId())
                .routeParam("mapId", getMapEnvReq.getMapId())
                .routeParam("envId", getMapEnvReq.getEnvId());
        if(getMapEnvReq.getMaxEnvs() != null)
            request.queryString("maxEnvs", getMapEnvReq.getMaxEnvs());
        if(getMapEnvReq.getVersionIdStart() != null)
            request.queryString("startAfter", getMapEnvReq.getVersionIdStart());
        String body = request.asString().getBody();
        GetMapEnvRes getMapEnvRes = GSON.fromJson(body, GetMapEnvRes.class);
        return getMapEnvRes;
    }

    public UploadReleaseSuccess uploadRelease(String userId, String mapId, String envId, InputStream fileInput) throws Exception{
        HttpRequestWithBody request = Unirest.post(url("/accounts/{userId}/maps/:mapId/env/{envId}"))
                .routeParam("userId", userId)
                .routeParam("mapId", mapId)
                .routeParam("envId", envId);
        request.field("file", fileInput, "file");
        String body = request.asString().getBody();
        UploadReleaseSuccess uploadReleaseSuccess = GSON.fromJson(body, UploadReleaseSuccess.class);
        return uploadReleaseSuccess;
    }
    private String url(String endpoint){
        return address + endpoint;
    }

    private void unZip(InputStream inputStream, File outputFolder) throws IOException {
        ZipUtil.unpack(inputStream, outputFolder);
    }
}
