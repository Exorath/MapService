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
public class GetMapsReq {
    private String userId;
    private String prefixFilter;
    private String startAfter;
    private Integer maxEnvs;

    public GetMapsReq(){

    }
    public GetMapsReq(String userId, String prefixFilter, String startAfter, Integer maxEnvs){
        this.userId = userId;
        this.prefixFilter = prefixFilter;
        this.startAfter = startAfter;
        this.maxEnvs = maxEnvs;
    }

    public String getUserId() {
        return userId;
    }

    public String getPrefixFilter() {
        return prefixFilter;
    }

    public String getStartAfter() {
        return startAfter;
    }

    public Integer getMaxEnvs() {
        return maxEnvs;
    }
}
