---
title: 'MIDDLEWARE Task 2: Apache Thrift'
author: "Adrian Pascan"
date: '2022-04-06'
output: html_document
---

# Setup (Thrift home directory)

When **running the server/client scripts**, for [each terminal instance]{.underline} you are using, please make sure to run the following commands beforehand:

-   `export LD_LIBRARY_PATH=/<THRIFT_HOME>/lib`

-   `export PATH="$PATH:/<THRIFT_HOME>/bin"`

where *\<THRIFT_HOME\>* is the home directory of the Apache Thrift library on your machine.

When **making the server** by using the *Makefile*, make sure to replace `<THRIFT_HOME>`in the `server` section of the *Makefile* with the home directory of the Apache Thrift library on your machine.

# Folders & files

Main **folders**:

-   *Task-Cpp-basic*: basic server;

-   *Task-Cpp-extended*: extended server;

-   *Task-Py-basic*: basic client;

-   *Task-Py-extended*: basic client.

The **updated *Task.thrift*** interface file may be found in the extended version of both the client and the server.

# Notice

On **summary** creation, because set is an unordered collection, in order to determinisically convert the set to a comma string I used the following algorithm: convert the set to a list, sort the list and call the comma string convertor with the sorted list as input.
