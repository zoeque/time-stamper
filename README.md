# Time stamper
## Overview
The library to create time stamp certification files easily.

## Settings
Add parameters bellow into application.properties.
```properties
zoeque.time.stamper.tsa.url=http://eswg.jnsa.org/freetsa
zoeque.time.stamper.hash=false
```  
The parameter `zoeque.time.stamper.tsa.url` is set the URL of TSA server.  
The parameter `zoeque.time.stamper.hash` set with boolean value and it can determine that the timestamp is created by the hashed file
based on the target file.  
The default flow performs;
```
Plane file -> create hashed file by SHA-256 -> create timestamp based on hashed file -> save both files
```