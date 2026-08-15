package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.UpdateProfileRequestDto;
import com.vermeulenkylian.backend.DTO.UserProfileDto;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserProfileDto getProfile(User user) {
        return new UserProfileDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getBio(),
                    user.getAvatarUrl(),
                    user.getCreatedAt()
            );
    }
    public UserProfileDto updateProfile(User user, UpdateProfileRequestDto dto) {
        user.setName(dto.getName());
        user.setBio(dto.getBio());
        userRepository.save(user);
        return getProfile(user);
    }
}
