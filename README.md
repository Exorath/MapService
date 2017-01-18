# MapService [![Build Status](https://travis-ci.org/Exorath/MapService.svg?branch=master)](https://travis-ci.org/Exorath/MapService)
The MapService is responsible for the uploading and downloading of map versions.

##Endpoints
###/accounts/{accountId}/maps
###Gets general information about all maps

**argument**
- accountId (string): the id of the account to fetch maps of.
- mapPrefix (string) [OPTIONAL]: only map names with this prefix will be returned.
- maxEnvs (int) [OPTIONAL]: the maximum amount of environments to return. The response will contain truncated=true if some are not returned.
- startAfter (string) [OPTIONAL]: after what map env pair to continue returning. This is useful for paging with maxEnvs. Format is '{mapId/{envId}', fe. 'map1/env1' .

**Response**:
```json
{
  "maps": {
    "map1": {
      "envs": {
        "dev": {
          "size": 5586321,
          "lastModified" : 1480178827038
        },
        "production": {
          "size": 4326402,
          "lastModified" : 1480178826024
        }
      }
    },"map2": {
        "envs": {
          "dev": {
            "size": 5586321,
            "lastModified" : 1480178827038
          }
        }
      }
  }
}
```
- truncated (boolean) [OPTIONAL]: true if there are more results which must be fetched in another request, defaults to false.
- maps (map of maps by their mapId)
  - envs (environments by their envId)
    - size (long): size of latest version in bytes
    - lastModified (UNIX timestamp): timestamp of date of latest upload

###/accounts/{accountId}/maps/{mapId}/env/{envId}/download [GET]
####Download the latest version (or specific version) of a map as a ZIP

**arguments**
- accountId (string): the id of the account to fetch maps of.
- mapId (string): the mapId to download an environment of
- envId (string): the envId of the mapId to download
- versionId (string) [OPTIONAL]: the versionId of the map version you wish to download


**Response**:
FileOutputStream with ZIP'd map contents

###/accounts/{accountId}/maps/{mapId}/env/{envId} [GET]
####Gets detailed information of the mapId of the environment

**Arguments**:
- accountId (string): the id of the account to fetch maps of.
- mapId (string): the mapId to fetch info of
- envId (string): the envId of the mapId to fetch info of
- maxEnvs (int) [OPTIONAL]: the maximum amount of environments to return. The response will contain truncated=true if some are not returned.
- startAfter (string) [OPTIONAL]: where in the sorted list of all versions to begin returning results. Results begin immediately after the version with the specified version ID. 

**Response**:
```json
{
  "truncated": true,
  "versions": {
    "111111":{
      "size": 5586321,
      "lastModified": 1480178827038
    }, 
    "222222":{
      "size": 5586321,
      "uploaded": 1480178827038,
      "latest": true
    }
  }
}
```
Returns {} if mapId/envId not found was not found.

- truncated (bool) [OPTIONAL]: Defaults to false, if true you can make a new call with the latest versionId in the version list to get the next page of versions
- versions (version map by versionId)
  - size (long): the size of this version in bytes
  - date (UNIX timestamp): timestamp of the upload time
  - latest (bool) [OPTIONAL]: Whether or not this is the used version (the default one)

###/accounts/{accountId}/maps/{mapId}/env/{env} [POST]
####Uploads a map version to the environment of the specified map, this will be used as the 'latest' version in the environment
A random versionId is generated for the save

**arguments**
- accountId (string): the id of the account to fetch maps of.

**Body**:
InputStream with ZIP'd map contents. 

**Response**:
```json
{"success": true}
```
- success (boolean): Whether or not the player will be teleported to a server.
- versionId (string): The just created versionId
- err (string)[OPTIONAL]: An error string that describes why the update failed. Only provided when success=*false*

##Environment
| Name | Value |
| --------- | --- |
| AWS_REGION | EU_CENTRAL_1 |
| AWS_ACCESS_KEY_ID	| {acces_key_id} |
| AWS_SECRET_KEY	| {secret_key} |
| PORT	| {PORT} |
| BUCKET_NAME | aws_bucket_name} |