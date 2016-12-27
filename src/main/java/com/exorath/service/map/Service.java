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

import com.exorath.service.map.res.*;

import java.io.InputStream;
import java.util.Collection;

/**
 * Created by toonsev on 11/26/2016.
 */
public interface Service {

    GetMapsRes getMaps(GetMapsReq getMapsReq);

    GetMapEnvRes getMapEnvInfo(GetMapEnvReq getMapEnvReq);

    InputStream downloadMap(DownloadMapReq downloadMapReq);


    UploadReleaseSuccess uploadRelease(String userId, String mapId, String envId, InputStream fileInput);

}
