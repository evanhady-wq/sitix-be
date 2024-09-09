package com.sitix.service;

import com.sitix.model.dto.request.CreatorRequest;
import com.sitix.model.dto.response.CreatorResponse;
import com.sitix.model.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CreatorService {
    void createCreator(CreatorRequest creatorRequest);
    CreatorResponse editCreator (CreatorRequest creatorRequest);
    void deleteAccount(String id);
    CreatorResponse viewCreatorProfile();
    CreatorResponse getById(String id);
    List<CreatorResponse> viewAllCreator();
    void deleteCreator(String id);
    List<CreatorResponse> findCreatorByName(String name);
    ImageResponse uploadProfile(MultipartFile file);
}
