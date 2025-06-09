package com.gamecraft.studios.service;

import com.gamecraft.studios.entity.Skill;
import com.gamecraft.studios.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Optional<Skill> findById(Long id) {
        return skillRepository.findById(id);
    }

    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }

    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public List<Skill> findByCategory(Skill.Category category) {
        return skillRepository.findByCategoryOrderByName(category);
    }

    public List<Skill> searchSkills(String name) {
        return skillRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Object[]> getPopularSkills() {
        return skillRepository.findPopularSkills();
    }

    // 기술 스택 생성 또는 기존 반환
    public Skill findOrCreateSkill(String skillName, Skill.Category category) {
        Optional<Skill> existing = skillRepository.findByName(skillName);
        if (existing.isPresent()) {
            return existing.get();
        }

        Skill newSkill = new Skill(skillName, category);
        return skillRepository.save(newSkill);
    }

    // 초기 기술 스택 데이터 생성
    public void initializeDefaultSkills() {
        if (skillRepository.count() > 0) {
            return; // 이미 데이터가 있으면 스킵
        }

        // 프로그래밍 언어
        saveSkill(new Skill("Java", Skill.Category.PROGRAMMING_LANGUAGE));
        saveSkill(new Skill("JavaScript", Skill.Category.PROGRAMMING_LANGUAGE));
        saveSkill(new Skill("TypeScript", Skill.Category.PROGRAMMING_LANGUAGE));
        saveSkill(new Skill("Python", Skill.Category.PROGRAMMING_LANGUAGE));
        saveSkill(new Skill("Kotlin", Skill.Category.PROGRAMMING_LANGUAGE));
        saveSkill(new Skill("Swift", Skill.Category.PROGRAMMING_LANGUAGE));

        // 프론트엔드
        saveSkill(new Skill("React", Skill.Category.FRONTEND));
        saveSkill(new Skill("Vue.js", Skill.Category.FRONTEND));
        saveSkill(new Skill("Angular", Skill.Category.FRONTEND));
        saveSkill(new Skill("Next.js", Skill.Category.FRONTEND));

        // 백엔드
        saveSkill(new Skill("Spring Boot", Skill.Category.BACKEND));
        saveSkill(new Skill("Node.js", Skill.Category.BACKEND));
        saveSkill(new Skill("Express.js", Skill.Category.BACKEND));
        saveSkill(new Skill("Django", Skill.Category.BACKEND));

        // 데이터베이스
        saveSkill(new Skill("PostgreSQL", Skill.Category.DATABASE));
        saveSkill(new Skill("MySQL", Skill.Category.DATABASE));
        saveSkill(new Skill("MongoDB", Skill.Category.DATABASE));
        saveSkill(new Skill("Redis", Skill.Category.DATABASE));

        // 데브옵스
        saveSkill(new Skill("Docker", Skill.Category.DEVOPS));
        saveSkill(new Skill("Kubernetes", Skill.Category.DEVOPS));
        saveSkill(new Skill("Jenkins", Skill.Category.DEVOPS));
        saveSkill(new Skill("Git", Skill.Category.DEVOPS));

        // 클라우드
        saveSkill(new Skill("AWS", Skill.Category.CLOUD));
        saveSkill(new Skill("Google Cloud", Skill.Category.CLOUD));
        saveSkill(new Skill("Azure", Skill.Category.CLOUD));
    }
}