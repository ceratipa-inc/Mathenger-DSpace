package com.example.mathengerapi.integrations.dspace.service;

import com.example.mathengerapi.integrations.dspace.model.Collection;
import com.example.mathengerapi.integrations.dspace.model.Community;
import com.example.mathengerapi.integrations.dspace.model.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "dspaceClient", url = "${dspace.api.base-url}")
public interface DSpaceClient {
    @GetMapping("/communities")
    List<Community> getCommunities();

    @GetMapping("/communities/{communityId}")
    Community getCommunityById(@PathVariable("communityId") UUID communityId);

    @GetMapping("/communities/{communityId}/collections")
    List<Collection> getCollectionsOfCommunity(@PathVariable("communityId") UUID communityId);

    @GetMapping("/collections")
    List<Collection> getCollections();

    @GetMapping("/collections/{collectionId}")
    Collection getCollectionById(@PathVariable("collectionId") UUID collectionId);

    @GetMapping("/collections/{collectionId}/items")
    List<Item> getItemsOfCollection(@PathVariable("collectionId") UUID collectionId);

}
