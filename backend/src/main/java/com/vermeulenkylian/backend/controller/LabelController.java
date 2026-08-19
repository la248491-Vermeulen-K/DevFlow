package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.CreateLabelRequestDto;
import com.vermeulenkylian.backend.DTO.LabelResponseDto;
import com.vermeulenkylian.backend.DTO.TaskResponseDto;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.service.LabelService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping
    public LabelResponseDto createLabel(@AuthenticationPrincipal User user, @PathVariable Long projectId, @RequestBody CreateLabelRequestDto dto) {
        return labelService.createLabel(user, projectId, dto);
    }
    @DeleteMapping("/{labelId}")
    public boolean deleteLabel(@AuthenticationPrincipal User user, @PathVariable Long labelId) {
        return labelService.deleteLabel(user, labelId);
    }
}
