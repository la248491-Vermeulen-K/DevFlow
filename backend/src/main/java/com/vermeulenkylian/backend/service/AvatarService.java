package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.UserProfileDto;
import com.vermeulenkylian.backend.exception.BadRequestException;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.repository.UserRepository;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@Service
public class AvatarService {

    UserRepository userRepository;

    public AvatarService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    public UserProfileDto uploadAvatar(User user, MultipartFile file) {
        Tika tika = new Tika();
        String detectedType = null;
        try {
            detectedType = tika.detect(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!Set.of("image/jpeg", "image/png", "image/webp").contains(detectedType)) {
            throw new BadRequestException("Invalid image type.");
        }
        if(file.getSize() <= 0 || file.getSize() > 2 * 1024 * 1024) {
            throw new BadRequestException("Invalid file size. File is empty or too large.");
        }
        String extension = switch (detectedType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new BadRequestException("Format is not supported");
        };

        Path uploadPath = Path.of(uploadDir);
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }

        String filename = UUID.randomUUID() + extension;

        try {
            file.transferTo(uploadPath.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Could not transfer file", e);
        }

        String avatarUrl = "/uploads/avatars/" + filename;
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        return new UserProfileDto(user.getId(), user.getName(), user.getEmail(), user.getBio(), user.getAvatarUrl(), user.getCreatedAt());
    }
}
