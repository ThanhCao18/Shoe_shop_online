package com.example.ananas.mapper;

import com.example.ananas.dto.request.UserCreateRequest;
import com.example.ananas.dto.request.UserUpdateRequest;
import com.example.ananas.dto.response.UserResponse;
import com.example.ananas.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    User toUser(UserCreateRequest userCreateRequest);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

}
