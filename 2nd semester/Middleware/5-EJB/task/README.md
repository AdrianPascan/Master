---
title: 'MIDDLEWARE Task 5: OpenEJB'
author: "Adrian Pascan"
date: '2022-05-07'
output: html_document
---

# General

I used **OpenEJB Standalone 8.0.6** for developing and testing the task.

## Setup

Please set the `OPENEJB_HOME` variable in the *setenv.sh* script to the home directory of your **OpenEJB** installation.

## Running the task

1.  Compile the beans and client: `bash make.sh`

2.  Run the EJB server (in another console window): `bash run-server.sh`

3.  Deploy the beans to the running server: `bash run-deploy.sh`

    -   If you want to undeploy the old version, use option `-u`

4.  Run the client: `bash run-client.sh`

    -   For the extended version add the client id as a command line argument

5.  Stop the OpenEJB server gracefully: `bash stop-server.sh`

## Versions

1.  *Basic* : single persisted graph

2.  *Extended* : multiple persisted graphs, each client having its own graph

# Documentation on Extended Version

The **client id** is given as a command line argument when running the client `Main`.

I extended the `Searcher` interface by adding a new method `boolean setClient(String clientId)` that sets the client id of the `Searcher` implementation to the variable `clientId` given as input parameter (assuming it is not null or an empty `String` instance). It is the **responsibility of the client** to set its id for the `Searcher` instance before calling any other method on this instance.

I changed the `SearcherImpl` to `@Statefull` so that a separate instance of `SearcherImpl` is bounded to each client `Main` instance.

I extended the `Node` implementation by adding a new property `clientId` that holds the id of the client. In this manner, each `Node` instance holds a reference to its client. The `SearcherImpl` class then adds `Node` instances using its `clientId` property and uses for queries not only the ids of the nodes, but the client id as well.

Thus, we can have **multiple clients at the same time with their own persisted graph** (without interfering with other clients' graphs).
