package com.sitix.service.Impl;

import com.sitix.constant.APIUrl;
import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.CreatorRequest;
import com.sitix.model.dto.response.CreatorResponse;
import com.sitix.model.dto.response.ImageResponse;
import com.sitix.model.entity.Creator;
import com.sitix.model.entity.Image;
import com.sitix.model.entity.User;
import com.sitix.service.CreatorService;
import com.sitix.service.FileStorageService;
import com.sitix.repository.CreatorRepository;
import com.sitix.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepository creatorRepository;
    private final FileStorageService fileStorageService;
    private final ImageRepository imageRepository;

    public void createCreator(CreatorRequest creatorRequest) {
        Creator creator = Creator.builder()
                .name(creatorRequest.getName())
                .introduction(creatorRequest.getIntroduction())
                .phone(creatorRequest.getPhone())
                .user(creatorRequest.getUser())
                .build();

        creatorRepository.saveAndFlush(creator);
    }


    public ImageResponse uploadProfile(MultipartFile file) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creatorFound = creatorRepository.findByUserId(loggedInUser.getId());
        String fileName = fileStorageService.storeFile(file, loggedInUser.getId());

        Image oldImage = creatorFound.getProfilePicture();

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(APIUrl.CREATOR_API)
                .path("/profilepicture/")
                .path(fileName)
                .toUriString();

        Image profilePicture = Image.builder()
                .name(fileName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .path(fileDownloadUri)
                .build();

        Image profilePictureSaved=imageRepository.saveAndFlush(profilePicture);

        creatorFound.setProfilePicture(profilePictureSaved);
        creatorRepository.save(creatorFound);

        if (oldImage != null) {
            imageRepository.delete(oldImage);
        }
        return ImageResponse.builder()
                .name(profilePicture.getName())
                .size(file.getSize())
                .contentType(profilePicture.getContentType())
                .path(profilePicture.getPath())
                .build();
    }

    public CreatorResponse editCreator (CreatorRequest creatorRequest) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedIn.getId());

        creator.setName(creatorRequest.getName());
        creator.setIntroduction(creatorRequest.getIntroduction());
        creator.setPhone(creatorRequest.getPhone());


        creatorRepository.saveAndFlush(creator);
        return convertToResponse(creator);
    }

    public void deleteAccount(String id) {
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Creator not found"));
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
        } else throw new ResourceNotFoundException("Creator still Active");
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
        String profilePicture;
        if (creator.getProfilePicture() == null){
            profilePicture = "";
        }else {
            profilePicture = creator.getProfilePicture().getPath();
        }
        return CreatorResponse.builder()
                .id(creator.getId())
                .name(creator.getName())
                .introduction(creator.getIntroduction())
                .phone(creator.getPhone())
                .profilePicture(profilePicture)
                .isDeleted(creator.getIsDeleted())
                .build();
    }
}
