package com.sitix.model.service.Impl;

import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.CreatorRequest;
import com.sitix.model.dto.response.CreatorResponse;
import com.sitix.model.entity.Creator;
import com.sitix.model.entity.User;
import com.sitix.model.service.CreatorService;
import com.sitix.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepository creatorRepository;

    public void createCreator(CreatorRequest creatorRequest) {
        Creator creator = Creator.builder()
                .name(creatorRequest.getName())
                .introduction(creatorRequest.getIntroduction())
                .phone(creatorRequest.getPhone())
                .user(creatorRequest.getUser())
                .build();

        creatorRepository.saveAndFlush(creator);
    }


    public CreatorResponse editCreator (CreatorRequest creatorRequest) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedIn.getId());

        creator.setName(creatorRequest.getName());
        creator.setIntroduction(creatorRequest.getIntroduction());
        creator.setPhone(creator.getPhone());

        creatorRepository.saveAndFlush(creator);
        return convertToResponse(creator);
    }

    public void deleteAccount() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());
        creator.setIsDeleted(true);

        creatorRepository.saveAndFlush(creator);
    }

    public CreatorResponse viewCreatorProfile() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());
        return convertToResponse(creator) ;
    }

    public CreatorResponse getById(String id) {
        return convertToResponse(findCreatorById(id));
    }

    public List<CreatorResponse> viewAllCreator() {
        return creatorRepository.findAll().stream().map(this::convertToResponse).toList();
    }


    public void deleteCreator(String id) {
        Creator creator = findCreatorById(id);
        if(creator.getIsDeleted().equals(true)){
            creatorRepository.delete(creator);
        } else throw new ResourceNotFoundException("Customer still Active");
    }

    public List<CreatorResponse> findCreatorByName(String name){
        try {
            return creatorRepository.findCreatorByNameContaining(name).stream().map(this::convertToResponse).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public Creator findCreatorById(String id) {
        return  creatorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Creator Not Found"));
    }

    private CreatorResponse convertToResponse(Creator creator) {
        return CreatorResponse.builder()
                .id(creator.getId())
                .name(creator.getName())
                .introduction(creator.getIntroduction())
                .phone(creator.getPhone())
                .build();
    }
}
