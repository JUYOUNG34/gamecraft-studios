package com.gamecraft.studios.service;

import com.gamecraft.studios.entity.JobPosition;
import com.gamecraft.studios.entity.JobPositionSkill;
import com.gamecraft.studios.entity.Skill;
import com.gamecraft.studios.repository.JobPositionSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobPositionSkillService {

    private final JobPositionSkillRepository jobPositionSkillRepository;
    private final SkillService skillService;

    public JobPositionSkillService(JobPositionSkillRepository jobPositionSkillRepository,
                                   SkillService skillService) {
        this.jobPositionSkillRepository = jobPositionSkillRepository;
        this.skillService = skillService;
    }

    public JobPositionSkill addSkillToJobPosition(JobPosition jobPosition, Skill skill,
                                                  boolean isRequired, Integer proficiencyLevel) {
        JobPositionSkill jps = new JobPositionSkill(jobPosition, skill, isRequired);
        jps.setProficiencyLevel(proficiencyLevel);
        return jobPositionSkillRepository.save(jps);
    }

    public List<JobPositionSkill> getJobPositionSkills(Long jobPositionId) {
        return jobPositionSkillRepository.findByJobPositionId(jobPositionId);
    }

    public List<JobPositionSkill> getRequiredSkills(Long jobPositionId) {
        return jobPositionSkillRepository.findRequiredSkillsByJobPosition(jobPositionId);
    }

    public List<JobPositionSkill> getPreferredSkills(Long jobPositionId) {
        return jobPositionSkillRepository.findPreferredSkillsByJobPosition(jobPositionId);
    }

    public void removeSkillFromJobPosition(Long jobPositionId, Long skillId) {
        List<JobPositionSkill> skills = jobPositionSkillRepository.findByJobPositionId(jobPositionId);
        skills.stream()
                .filter(jps -> jps.getSkill().getId().equals(skillId))
                .forEach(jobPositionSkillRepository::delete);
    }

    // 문자열 기술 스택을 JobPositionSkill로 변환
    public void convertStringSkillsToEntities(JobPosition jobPosition, String skillsString,
                                              boolean isRequired) {
        if (skillsString == null || skillsString.trim().isEmpty()) {
            return;
        }

        String[] skillNames = skillsString.split("[,|]");
        for (String skillName : skillNames) {
            skillName = skillName.trim();
            if (!skillName.isEmpty()) {
                // 기본 카테고리로 PROGRAMMING_LANGUAGE 설정 (나중에 개선 가능)
                Skill skill = skillService.findOrCreateSkill(skillName, Skill.Category.PROGRAMMING_LANGUAGE);
                addSkillToJobPosition(jobPosition, skill, isRequired, null);
            }
        }
    }
}