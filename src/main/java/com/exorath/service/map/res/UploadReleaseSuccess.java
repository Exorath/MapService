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
 * Created by toonsev on 12/3/2016.
 */
public class UploadReleaseSuccess extends Success {
    private String versionId;
    public UploadReleaseSuccess(boolean success){
        super(success);
    }
    public UploadReleaseSuccess(boolean success, String error){
        super(success, error);
    }

    public UploadReleaseSuccess(String versionId){
        super(true);
        this.versionId = versionId;
    }

    public String getVersionId() {
        return versionId;
    }
}
