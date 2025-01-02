package com.example.ananas.mapper;

import com.example.ananas.dto.request.UserCreateRequest;
import com.example.ananas.dto.request.UserUpdateRequest;
import com.example.ananas.dto.response.UserResponse;
import com.example.ananas.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class IUserMapperImpl implements IUserMapper {

    @Override
    public User toUser(UserCreateRequest userCreateRequest) {
        if ( userCreateRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( userCreateRequest.getUsername() );
        user.password( userCreateRequest.getPassword() );
        user.email( userCreateRequest.getEmail() );
        user.phone( userCreateRequest.getPhone() );
        user.address( userCreateRequest.getAddress() );
        user.firstname( userCreateRequest.getFirstname() );
        user.lastname( userCreateRequest.getLastname() );

        return user.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.userId( user.getUserId() );
        userResponse.username( user.getUsername() );
        userResponse.email( user.getEmail() );
        userResponse.phone( user.getPhone() );
        userResponse.address( user.getAddress() );
        userResponse.firstname( user.getFirstname() );
        userResponse.lastname( user.getLastname() );
        userResponse.avatar( user.getAvatar() );
        userResponse.isActive( user.getIsActive() );

        return userResponse.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest userUpdateRequest) {
        if ( userUpdateRequest == null ) {
            return;
        }

        user.setEmail( userUpdateRequest.getEmail() );
        user.setPhone( userUpdateRequest.getPhone() );
        user.setAddress( userUpdateRequest.getAddress() );
        user.setFirstname( userUpdateRequest.getFirstname() );
        user.setLastname( userUpdateRequest.getLastname() );
    }
}
