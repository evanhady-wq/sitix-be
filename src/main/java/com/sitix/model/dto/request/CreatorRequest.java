package com.sitix.model.dto.request;

import com.sitix.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorRequest {
    private String id;
    private String name;
    private String phone;
    private String introduction;
    private User user;
    private String pathFile;
}
