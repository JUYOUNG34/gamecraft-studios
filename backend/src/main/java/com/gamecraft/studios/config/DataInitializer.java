package com.gamecraft.studios.config;

import com.gamecraft.studios.entity.Company;
import com.gamecraft.studios.service.CompanyService;
import com.gamecraft.studios.service.SkillService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner initializeData(SkillService skillService, CompanyService companyService) {
        return args -> {
            // 기본 기술 스택 초기화
            skillService.initializeDefaultSkills();

            // 기본 회사 데이터 초기화
            initializeDefaultCompanies(companyService);
        };
    }

    private void initializeDefaultCompanies(CompanyService companyService) {
        if (companyService.findAll().isEmpty()) {
            // 게임 회사들
            companyService.saveCompany(new Company(
                    "카카오게임즈",
                    "/images/companies/kakao-games.png",
                    "글로벌 게임 퍼블리싱 전문 기업"
            ));

            companyService.saveCompany(new Company(
                    "넥슨",
                    "/images/companies/nexon.png",
                    "온라인 게임 개발 및 서비스 전문 기업"
            ));

            companyService.saveCompany(new Company(
                    "엔씨소프트",
                    "/images/companies/ncsoft.png",
                    "MMORPG 전문 게임 개발사"
            ));

            // IT 회사들
            companyService.saveCompany(new Company(
                    "네이버",
                    "/images/companies/naver.png",
                    "국내 최대 포털 및 IT 서비스 기업"
            ));

            companyService.saveCompany(new Company(
                    "카카오",
                    "/images/companies/kakao.png",
                    "모바일 플랫폼 및 콘텐츠 서비스 기업"
            ));

            companyService.saveCompany(new Company(
                    "라인",
                    "/images/companies/line.png",
                    "글로벌 메신저 및 플랫폼 서비스 기업"
            ));
        }
    }
}