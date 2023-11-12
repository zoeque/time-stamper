# Time stamper
## Overview
The library to create time stamp response files easily.

## Structure  
- lib
  - Main processes are included in this directory. This directory has only the process, no main class.  
- testapp
  - The test application to check the process using JUnit testing codes.

## Processes and Architecture
The time-stamper provides the function to create the timestamp response file by requesting the timestamp CA 
by HTTP message.  
Creation is executed by using BouncyCastle library with the version defined in build.gradle file.  

The architecture of this library is based on the clean architecture, with an Adapter layer, usecase layer, and repository layer.  
This module does not use the Database, so there is no repository layer class, but only adapter classes performs as a interface to
for read and write files, send request to the timestamp CA server and the service classes to create the request message.


## Usage
First of all, create instance of `TimeStampRequestService` as a Bean of the main application,
or create new instance of the class.  
Call `createTimeStamp(String)` method and give the file directory as an absolute path to this method.  
Created timestamp is returned with the result Try, provided by the vavr.io library.

## Settings
Add parameters bellow into application.properties.
```properties
zoeque.time.stamper.tsa.url=http://eswg.jnsa.org/freetsa
```  
The parameter `zoeque.time.stamper.tsa.url` is set the URL of TSA server.  
The default flow performs;
```
Plane file -> create hashed file by SHA-256 -> create timestamp based on hashed file -> save both files
```  

