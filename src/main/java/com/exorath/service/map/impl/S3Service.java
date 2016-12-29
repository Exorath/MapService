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

package com.exorath.service.map.impl;

import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.exorath.service.commons.dynamoDBProvider.DynamoDBProvider;
import com.exorath.service.commons.tableNameProvider.TableNameProvider;
import com.exorath.service.map.Service;
import com.exorath.service.map.res.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by toonsev on 12/25/2016.
 */
public class S3Service implements Service {
    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private AmazonS3Client amazonS3Client;
    private String bucketName;

    public S3Service(DynamoDBProvider dbProvider, TableNameProvider bucketNameProvider) {
        setupAmazonS3(dbProvider, bucketNameProvider);
    }

    private void setupAmazonS3(DynamoDBProvider dbProvider, TableNameProvider bucketNameProvider) {
        amazonS3Client = new AmazonS3Client(dbProvider.getCredentials());
        amazonS3Client.setRegion(Region.getRegion(dbProvider.getRegion()));
        bucketName = bucketNameProvider.getTableName();
        try {
            amazonS3Client.createBucket(bucketName);
        }catch(AmazonS3Exception e){
            if(e.getStatusCode() != 409)//Bucket already created
                throw e;
        }
        //Enable versioning
        amazonS3Client.setBucketVersioningConfiguration(new SetBucketVersioningConfigurationRequest(bucketName,
                new BucketVersioningConfiguration()
                        .withStatus("Enabled")
        ));
    }

    //implemented.
    @Override
    public GetMapsRes getMaps(GetMapsReq getMapsReq) {
        String prefix = format(getMapsReq.getUserId(), getMapsReq.getPrefixFilter());
        ListObjectsV2Request request = new ListObjectsV2Request();
        request.setBucketName(bucketName);
        request.setPrefix(prefix);
        request.setStartAfter(getMapsReq.getUserId() + "/" + getMapsReq.getStartAfter());
        request.setMaxKeys(getMapsReq.getMaxEnvs());

        ListObjectsV2Result result = amazonS3Client.listObjectsV2(request);
        GetMapsRes getMapsRes = new GetMapsRes(result.isTruncated());
        //Go over all objects (environments) and add them to their mapInfo (or create mapInfo if nonexistent), add these to a GetMapsRes
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            String[] parts = objectSummary.getKey().split("/"); //format: '{accountId}/{mapId}/{envId}'
            if (parts.length != 3) {
                LOG.warn("Found object in bucket not with format {accountId}/{mapId}/{envId}. Skipping it.");
                continue;
            }
            MapEnv mapEnv = new MapEnv(objectSummary.getLastModified().getTime(), objectSummary.getSize());
            MapInfo mapInfo = getMapsRes.getMaps().get(parts[1]);
            if(mapInfo == null)
                mapInfo = new MapInfo();
            mapInfo.getEnvironments().put(parts[2], mapEnv);
        }
        return getMapsRes;
    }

    //implemented.
    @Override
    public GetMapEnvRes getMapEnvInfo(GetMapEnvReq getMapEnvReq) {
        if(getMapEnvReq.getUserId() == null || getMapEnvReq.getMapId() == null || getMapEnvReq.getEnvId() == null)
            throw new IllegalArgumentException("UserId, mapId or envId not provided");
        ListVersionsRequest request = new ListVersionsRequest()
                .withBucketName(bucketName)
                .withPrefix(format(getMapEnvReq.getUserId(), getMapEnvReq.getMapId(), getMapEnvReq.getEnvId()))
                .withVersionIdMarker(
                        getMapEnvReq.getVersionIdStart())
                .withMaxResults(getMapEnvReq.getMaxEnvs());
        VersionListing versionListing = amazonS3Client.listVersions(request);
        GetMapEnvRes mapEnvRes = new GetMapEnvRes(versionListing.isTruncated());

        for(S3VersionSummary versionSummary :  versionListing.getVersionSummaries()){
            String versionId = versionSummary.getVersionId();
            Boolean latest = versionSummary.isLatest() ? true : null;
            mapEnvRes.getVersions().put(versionId, new MapVersion(versionSummary.getLastModified().getTime(), versionSummary.getSize(), latest));
        }
        return mapEnvRes;
    }


    @Override
    public InputStream downloadMap(DownloadMapReq downloadMapReq) {
        GetObjectRequest request = new GetObjectRequest(bucketName, format(downloadMapReq.getUserId(), downloadMapReq.getMapId(), downloadMapReq.getEnvId()));
        request.setVersionId(downloadMapReq.getVersionId());
        S3Object object = amazonS3Client.getObject(request);
        return object == null ? null : object.getObjectContent();
    }
    @Override
    public UploadReleaseSuccess uploadRelease(String accountId, String mapId, String envId, InputStream fileInput) {
        accountId = replaceSlashes(accountId);
        mapId = replaceSlashes(mapId);
        envId = replaceSlashes(envId);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, format(accountId, mapId, envId), fileInput, new ObjectMetadata());
        PutObjectResult result = amazonS3Client.putObject(putObjectRequest);
        return result.getVersionId() == null ? new UploadReleaseSuccess(false) : new UploadReleaseSuccess(result.getVersionId());
    }
    public static String format(String userId, String... nextParts){
        String formatted = replaceSlashes(userId);
        for(String s : nextParts){
            formatted += "/" + replaceSlashes(s);
        }
        return formatted;
    }
    private static final String replaceSlashes(String original) {

        return original == null ? null : original.replaceAll("/", "_");
    }
}
