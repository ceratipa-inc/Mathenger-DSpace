package com.example.mathengerapi.integrations.dspace.service;

import com.example.mathengerapi.integrations.dspace.model.Community;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "dspaceClient", url = "${dspace.api.base-url}")
public interface DSpaceClient {
    @GetMapping("/communities")
    List<Community> getCommunities();
}
