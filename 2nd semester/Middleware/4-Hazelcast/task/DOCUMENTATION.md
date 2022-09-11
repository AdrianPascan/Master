---
title: 'MIDDLEWARE Task 4: Hazelcast'
author: "Adrian Pascan"
date: '2022-04-23'
output: html_document
---

# General

I used **Hazelcast 4.2.4** for running and testing the task.

**Two variants**, both related to the way in which we keep track of the next favorite document:

-   one using the index of the next favorite in the favorites list;

-   one using the name of the next favorite document.

# Feature mentions

## Remove favorite

If the selected document to be removed on the remove favorite call is in the favorite list, the next favorite cycle is restarted and the selected document is reset to none.

# Configuration

## Single cluster with multiple members

Because in the configuration file (*hazelcast.yaml*) I set the `join: multicast: enabled` to false, **only one cluster** will be created and **any new member** will be part of this cluster, in this manner: 1st member on `127.0.0.1:5701`, 2nd member on `127.0.0.1:5702` and so on. They listen on the localhost (`127.0.0.1`), but on different ports which are sequentially assigned starting from 5701.

## Map configurations

The cache map `Documents` supports

### Backup count

I set `backup-count: 0` for the following maps because they can tolerate data loss:

-   the cache map `Documents` for which the lost document can be generated again;

-   the map `UserNextFavorite` for which the lost reference to the next favorite document can be solved by restarting the favorite documents cycle;

-   the map `UserSelectedDocument` for which the lost reference to the currently selected document can be reset by having no document selected.

I kept the default, i.e. `backup-count: 1`, fo the other maps because they cannot tolerate data loss, they should keep the client data even if the client disconnected as it may reconnect and its last state should be restored.

### Time To Live (TTL)

When setting `time-to-live-seconds: x` for a map configuration, any entry will be discarded *x* seconds after the last write.

I set `time-to-live-seconds: 30` for the cache map `Documents` so that after 30 seconds have passed since the document was lastly written it would be discarded and generated again when requested, thus resembling a cache.

I set `time-to-live-seconds: 1800` for the maps `UserNextFavorite`/`UserFavoriteIndex` (depending on the variant - see General section) so that after half an hour has passed since the next favorite/selected document was lastly written it would be discarded as it does not represent critical data and it can be reset as described for backup count.

I kept the default, i.e. `time-to-live-seconds: 0` (infinite, according to the Hazelcast documentation), for the other maps as the map entries cannot be discarded again, as in the case of backup count, because of data persistence and consistency.

### Max Idle

When setting `max-idle-seconds: x` for a map configuration, any entry will be discarded *x* seconds after the last read or write.

I set the same values as I did for `time-to-live-seconds` for the same reason, except that for the cache map `Documents` I used the value 15 so that the cached documents that are not read get discarded earlier.

### Eviction policy

I set `eviction-policy: LRU` for the maps that have `time-to-live-seconds` and `max-idle-seconds` not set to the default (infinite) - namely `Documents`, `UserNextFavorite`/`UserFavoriteIndex` (depending on the variant - see General section) and `UserSelectedDocument` - so that when the default max size is reached, the least recently used (LRU) entries will be discarded because it indicates lack of usage from the user on these specific resources.

I kept the default, i.e. `eviction-policy: NONE`, so that no items are evicted because of the same reasons I stated for backup count.
