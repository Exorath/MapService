# MapService
The MapService is responsible for the uploading and downloading of map versions.

##Endpoints
###/maps
###Gets general information about all maps

**argument**
- mapPrefix (string): only map names with this prefix will be returned.

**Response**:
```json
{ 
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
```


###/maps/{mapId}/env/{env}/download [GET]
####Gets general information about the mapId

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
  "versions": {
    "111111":{
      "size": 5586321,
      "lastModified": 1480178827038
    }, 
    "222222":{
      "size": 5586321,
      "lastModified": 1480178827038,
      "latest": true
    }
  }
}
```
Returns {} if mapId/envId not found was not found.

- mapId (string): the id of this map
- envId (string): the id of this environment
- download (string): HTTP address to download the latest zip of this world.
- version (int): the version of the map within this environment
- lastModified (UNIX timestamp): timestamp of the latest upload time
- versions (int array): an array of the version history (TODO: Allow a limit for this)

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