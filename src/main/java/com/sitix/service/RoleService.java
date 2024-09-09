package com.sitix.service;

import com.sitix.model.entity.Role;

public interface RoleService {
    Role getOrSave(Role.UserRole role);

}
