package com.gamecraft.studios.controller;

import com.gamecraft.studios.entity.Skill;
import com.gamecraft.studios.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSkills() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Skill> skills = skillService.findAll();
            response.put("success", true);
            response.put("skills", skills);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "기술 스택 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getSkillsByCategory(@PathVariable String category) {
        Map<String, Object> response = new HashMap<>();

        try {
            Skill.Category skillCategory = Skill.Category.valueOf(category.toUpperCase());
            List<Skill> skills = skillService.findByCategory(skillCategory);
            response.put("success", true);
            response.put("skills", skills);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "잘못된 카테고리입니다");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "기술 스택 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularSkills() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Object[]> popularSkills = skillService.getPopularSkills();
            response.put("success", true);
            response.put("skills", popularSkills);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "인기 기술 스택 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchSkills(@RequestParam String query) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Skill> skills = skillService.searchSkills(query);
            response.put("success", true);
            response.put("skills", skills);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "기술 스택 검색 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}