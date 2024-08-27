package com.sitix.model.service.Impl;

import com.sitix.model.entity.Role;
import com.sitix.model.service.RoleService;
import com.sitix.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(Role.UserRole role) {
        Optional<Role> theRoles = roleRepository.findByName(role);
        if(theRoles.isPresent()){
            return theRoles.get();
        }
        Role currentRole = Role.builder()
                .name(role)
                .build();
        return roleRepository.saveAndFlush(currentRole);
    }
}
