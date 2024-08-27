package com.sitix.model.service;

import com.sitix.model.dto.request.CreatorRequest;
import com.sitix.model.dto.response.CreatorResponse;

import java.util.List;

public interface CreatorService {
    void createCreator(CreatorRequest creatorRequest);
    CreatorResponse editCreator (CreatorRequest creatorRequest);
    void deleteAccount();
    CreatorResponse viewCreatorProfile();
    CreatorResponse getById(String id);
    List<CreatorResponse> viewAllCreator();
    void deleteCreator(String id);
    List<CreatorResponse> findCreatorByName(String name);
}
