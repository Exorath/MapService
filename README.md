# MapService
The MapService is responsible for the uploading and downloading of map versions.

##Endpoints
###/maps
###Gets general information about all maps

**argument**
- mapPrefix (string) [OPTIONAL]: only map names with this prefix will be returned.

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
- maps (map of maps by their mapId)
  - envs (environments by their envId)
    - size (long): size of latest version in bytes
    - lastModified (UNIX timestamp): timestamp of date of latest upload

###/maps/{mapId}/env/{env}/download [GET]
####Download the latest version (or specific version) of a map as a ZIP

**arguments**
- versionId (string) [OPTIONAL]: the versionId of the map version you wish to download

**Response**:
FileOutputStream with ZIP'd map contents

###/maps/{mapId}/env/{env} [GET]
####Gets detailed information of the mapId of the environment

**Arguments**:
- idMarker (string) [OPTIONAL]: where in the sorted list of all versions in the specified bucket to begin returning results. Results begin immediately after the version with the specified version ID. 
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

###/maps/{mapId}/env/{env} [POST]
####Uploads a map version to the environment of the specified map, this will be used as the 'latest' version in the environment
A random versionId is generated for the save

**Body**:
FileOutputStream with ZIP'd map contents. 

**Response**:
```json
{"success": true}
```
- success (boolean): Whether or not the player will be teleported to a server.
- err (string)[OPTIONAL]: An error string that describes why the update failed. Only provided when success=*false*