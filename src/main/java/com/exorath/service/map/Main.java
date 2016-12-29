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

import com.exorath.service.commons.dynamoDBProvider.DynamoDBProvider;
import com.exorath.service.commons.portProvider.PortProvider;
import com.exorath.service.commons.tableNameProvider.TableNameProvider;
import com.exorath.service.map.impl.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by toonsev on 11/26/2016.
 */
public class Main {
    private Service service;
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private Main() {
        this.service = new S3Service(DynamoDBProvider.getEnvironmentDynamoDBProvider(), TableNameProvider.getEnvironmentTableNameProvider("BUCKET_NAME"));//TODO: Set service impl
        LOG.info("Service " + this.service.getClass() + " instantiated");
        Transport.setup(service, PortProvider.getEnvironmentPortProvider());
        LOG.info("HTTP Transport initiated");
    }

    public static void main(String[] args) {
        new Main();
    }
}
