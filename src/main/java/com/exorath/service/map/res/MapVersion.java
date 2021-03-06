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
public class MapVersion {
    private Long lastModified;
    private Long size;
    private Boolean latest;

    public MapVersion() {}

    public MapVersion(Long lastModified, Long size, Boolean latest) {
        this.lastModified = lastModified;
        this.size = size;
        this.latest = latest;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public Long getSize() {
        return size;
    }


}
