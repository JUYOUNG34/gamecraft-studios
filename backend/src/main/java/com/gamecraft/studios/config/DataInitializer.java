package com.gamecraft.studios.config;

import com.gamecraft.studios.entity.JobPosition;
import com.gamecraft.studios.repository.JobPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 있으면 초기화하지 않음
        if (jobPositionRepository.count() > 0) {
            return;
        }

        createSampleJobPositions();
    }

    private void createSampleJobPositions() {
        List<JobPosition> positions = Arrays.asList(
                createJobPosition(
                        "카카오게임즈",
                        "웹 플랫폼 개발자",
                        "게임 플랫폼의 웹 서비스 개발 및 운영을 담당합니다.\n" +
                                "- React/Vue.js 기반 프론트엔드 개발\n" +
                                "- Node.js/Spring Boot 기반 백엔드 API 개발\n" +
                                "- 게임 데이터 분석 도구 개발\n" +
                                "- 사용자 경험 개선 및 성능 최적화",
                        "- 컴퓨터공학 또는 관련 학과 학사 이상\n" +
                                "- React, Vue.js 등 모던 프론트엔드 프레임워크 경험 2년 이상\n" +
                                "- JavaScript/TypeScript 능숙\n" +
                                "- RESTful API 설계 및 개발 경험",
                        "React,TypeScript,Node.js,AWS,Docker",
                        "Vue.js,Kubernetes,Redis,GraphQL",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.MIDDLE,
                        "판교",
                        "5000-7000만원",
                        true,
                        LocalDateTime.now().plusDays(30),
                        "4대보험,연차,교육비지원,점심제공,야근택시"
                ),

                createJobPosition(
                        "Krafton",
                        "게임 클라이언트 개발자",
                        "PUBG와 같은 대규모 멀티플레이어 게임의 클라이언트 개발을 담당합니다.\n" +
                                "- Unity/Unreal Engine 기반 게임 클라이언트 개발\n" +
                                "- 게임플레이 시스템 설계 및 구현\n" +
                                "- 성능 최적화 및 디버깅\n" +
                                "- 크로스플랫폼 개발 (PC, 모바일, 콘솔)",
                        "- 게임 개발 경험 3년 이상\n" +
                                "- Unity 또는 Unreal Engine 전문 지식\n" +
                                "- C#/C++ 프로그래밍 능숙\n" +
                                "- 3D 그래픽스 및 렌더링 파이프라인 이해",
                        "Unity,C#,C++,Unreal Engine",
                        "DirectX,OpenGL,Vulkan,HLSL",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.SENIOR,
                        "서울",
                        "7000-10000만원",
                        false,
                        LocalDateTime.now().plusDays(25),
                        "4대보험,연차,주식옵션,헬스장,카페테리아"
                ),

                createJobPosition(
                        "Pearl Abyss",
                        "백엔드 개발자",
                        "검은사막과 같은 MMORPG의 서버 인프라 개발 및 운영을 담당합니다.\n" +
                                "- Java/Spring Boot 기반 게임 서버 개발\n" +
                                "- 대용량 트래픽 처리를 위한 시스템 설계\n" +
                                "- 마이크로서비스 아키텍처 구축\n" +
                                "- 데이터베이스 최적화 및 캐싱 전략 수립",
                        "- Java/Spring 프레임워크 경험 3년 이상\n" +
                                "- MySQL, PostgreSQL 등 RDBMS 경험\n" +
                                "- Redis, MongoDB 등 NoSQL 경험\n" +
                                "- AWS, Docker, Kubernetes 활용 경험",
                        "Java,Spring Boot,MySQL,Redis,AWS",
                        "Kafka,Elasticsearch,Prometheus,Grafana",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.SENIOR,
                        "안양",
                        "6000-8000만원",
                        true,
                        LocalDateTime.now().plusDays(20),
                        "4대보험,연차,성과급,교육비지원,게임기지급"
                ),

                createJobPosition(
                        "NCSoft",
                        "AI 엔지니어",
                        "게임 내 AI 시스템 개발 및 데이터 분석을 담당합니다.\n" +
                                "- 게임 AI 알고리즘 설계 및 구현\n" +
                                "- 머신러닝 모델 개발 및 서비스화\n" +
                                "- 게임 데이터 분석 및 지표 관리\n" +
                                "- A/B 테스트 설계 및 분석",
                        "- 컴퓨터공학, 수학, 통계학 학사 이상\n" +
                                "- Python, TensorFlow/PyTorch 경험\n" +
                                "- 머신러닝/딥러닝 프로젝트 경험\n" +
                                "- SQL 및 데이터 분석 도구 활용 능력",
                        "Python,TensorFlow,PyTorch,SQL,Docker",
                        "Kubernetes,Spark,Airflow,MLflow",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.MIDDLE,
                        "서울",
                        "6000-9000만원",
                        true,
                        LocalDateTime.now().plusDays(35),
                        "4대보험,연차,연구비지원,논문발표지원,컨퍼런스참석지원"
                ),

                createJobPosition(
                        "Smilegate",
                        "DevOps 엔지니어",
                        "게임 서비스의 인프라 구축 및 운영 자동화를 담당합니다.\n" +
                                "- CI/CD 파이프라인 구축 및 관리\n" +
                                "- 클라우드 인프라 설계 및 운영\n" +
                                "- 모니터링 및 알람 시스템 구축\n" +
                                "- 서비스 안정성 및 성능 향상",
                        "- Linux 시스템 관리 경험 3년 이상\n" +
                                "- AWS, GCP 등 클라우드 플랫폼 경험\n" +
                                "- Docker, Kubernetes 운영 경험\n" +
                                "- Infrastructure as Code (Terraform 등) 경험",
                        "AWS,Docker,Kubernetes,Terraform,Jenkins",
                        "Ansible,Prometheus,Grafana,ELK Stack",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.SENIOR,
                        "서울",
                        "7000-9500만원",
                        true,
                        LocalDateTime.now().plusDays(15),
                        "4대보험,연차,인증서지원,교육비지원,야근택시"
                ),

                createJobPosition(
                        "Nexon",
                        "프론트엔드 개발자",
                        "게임 웹사이트 및 관리 도구의 프론트엔드 개발을 담당합니다.\n" +
                                "- React/Next.js 기반 웹 애플리케이션 개발\n" +
                                "- 반응형 웹 디자인 구현\n" +
                                "- 성능 최적화 및 SEO 개선\n" +
                                "- 디자이너, 백엔드 개발자와의 협업",
                        "- React 개발 경험 2년 이상\n" +
                                "- JavaScript/TypeScript 능숙\n" +
                                "- HTML5, CSS3, Sass/SCSS 활용 능력\n" +
                                "- Git을 활용한 협업 경험",
                        "React,TypeScript,Next.js,Sass,Webpack",
                        "Vue.js,Nuxt.js,Tailwind CSS,Storybook",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.JUNIOR,
                        "서울",
                        "4000-6000만원",
                        true,
                        LocalDateTime.now().plusDays(40),
                        "4대보험,연차,점심제공,스낵바,게임룸"
                ),

                createJobPosition(
                        "넷마블",
                        "QA 엔지니어",
                        "모바일 게임의 품질 보증 및 테스트 자동화를 담당합니다.\n" +
                                "- 게임 기능 테스트 및 버그 발견\n" +
                                "- 테스트 자동화 스크립트 작성\n" +
                                "- 테스트 케이스 설계 및 관리\n" +
                                "- 개발팀과의 협업을 통한 품질 개선",
                        "- QA 또는 테스트 경험 2년 이상\n" +
                                "- 모바일 게임 테스트 경험\n" +
                                "- 테스트 자동화 도구 사용 경험\n" +
                                "- 버그 추적 도구 (Jira 등) 사용 경험",
                        "Selenium,Appium,Jira,TestRail,Python",
                        "Postman,Charles,Jenkins,Git",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.MIDDLE,
                        "서울",
                        "4500-6500만원",
                        false,
                        LocalDateTime.now().plusDays(28),
                        "4대보험,연차,교육비지원,건강검진,리프레시휴가"
                ),

                createJobPosition(
                        "컴투스",
                        "게임 기획자",
                        "모바일 RPG 게임의 기획 및 운영을 담당합니다.\n" +
                                "- 게임 시스템 기획 및 밸런싱\n" +
                                "- 유저 데이터 분석 및 개선 방안 도출\n" +
                                "- 이벤트 기획 및 운영\n" +
                                "- 개발팀과의 협업을 통한 기능 구현",
                        "- 게임 기획 경험 2년 이상\n" +
                                "- 모바일 게임에 대한 이해\n" +
                                "- 데이터 분석 능력 (Excel, SQL 등)\n" +
                                "- 문서 작성 및 커뮤니케이션 능력",
                        "Excel,SQL,Tableau,Figma,Confluence",
                        "Python,Google Analytics,Firebase",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.MIDDLE,
                        "서울",
                        "5000-7000만원",
                        false,
                        LocalDateTime.now().plusDays(22),
                        "4대보險,연차,성과급,점심제공,워크샵"
                ),

                createJobPosition(
                        "카카오게임즈",
                        "데이터 분석가",
                        "게임 서비스의 데이터 분석 및 인사이트 도출을 담당합니다.\n" +
                                "- 게임 지표 분석 및 리포트 작성\n" +
                                "- A/B 테스트 설계 및 결과 분석\n" +
                                "- 대시보드 구축 및 자동화\n" +
                                "- 비즈니스 전략 수립 지원",
                        "- 데이터 분석 경험 2년 이상\n" +
                                "- SQL, Python/R 능숙\n" +
                                "- 통계학 기본 지식\n" +
                                "- 시각화 도구 (Tableau, Power BI 등) 경험",
                        "SQL,Python,Tableau,Excel,Jupyter",
                        "R,Spark,Airflow,Google Analytics",
                        JobPosition.JobType.FULL_TIME,
                        JobPosition.ExperienceLevel.JUNIOR,
                        "판교",
                        "4500-6500만원",
                        true,
                        LocalDateTime.now().plusDays(32),
                        "4대보험,연차,교육비지원,점심제공,카페테리아"
                ),

                createJobPosition(
                        "펍지",
                        "게임 서버 개발자 (인턴)",
                        "배틀그라운드의 게임 서버 개발 인턴십 프로그램입니다.\n" +
                                "- C++ 기반 게임 서버 개발 참여\n" +
                                "- 네트워크 프로그래밍 학습\n" +
                                "- 시니어 개발자 멘토링\n" +
                                "- 실제 서비스 운영 경험",
                        "- 컴퓨터공학 관련 학과 재학/졸업\n" +
                                "- C/C++ 프로그래밍 기초\n" +
                                "- 네트워크 프로그래밍에 대한 관심\n" +
                                "- 게임에 대한 열정",
                        "C++,TCP/IP,Linux,Git,CMake",
                        "Redis,PostgreSQL,Docker,gRPC",
                        JobPosition.JobType.INTERN,
                        JobPosition.ExperienceLevel.JUNIOR,
                        "서울",
                        "300만원 (월)",
                        false,
                        LocalDateTime.now().plusDays(45),
                        "4대보험,중식제공,교육프로그램,멘토링,정규직전환기회"
                )
        );

        jobPositionRepository.saveAll(positions);
        System.out.println("✅ " + positions.size() + "개의 샘플 채용공고가 생성되었습니다!");
    }

    private JobPosition createJobPosition(String company, String title, String description,
                                          String requirements, String requiredSkills, String preferredSkills,
                                          JobPosition.JobType jobType, JobPosition.ExperienceLevel experienceLevel,
                                          String location, String salaryRange, boolean remoteWork,
                                          LocalDateTime deadline, String benefits) {
        JobPosition position = new JobPosition();
        position.setCompany(company);
        position.setTitle(title);
        position.setDescription(description);
        position.setRequirements(requirements);
        position.setRequiredSkills(requiredSkills);
        position.setPreferredSkills(preferredSkills);
        position.setJobType(jobType);
        position.setExperienceLevel(experienceLevel);
        position.setLocation(location);
        position.setSalaryRange(salaryRange);
        position.setRemoteWorkAvailable(remoteWork);
        position.setApplicationDeadline(deadline);
        position.setBenefits(benefits);
        position.setStatus(JobPosition.Status.ACTIVE);
        position.setViewCount((int) (Math.random() * 500) + 50); // 50-550 랜덤 조회수
        position.setApplicationCount((int) (Math.random() * 30) + 5); // 5-35 랜덤 지원자수

        // 슬러그 생성 (간단하게)
        position.setSlug(company.toLowerCase().replaceAll("\\s+", "-") + "-" +
                title.toLowerCase().replaceAll("\\s+", "-").replaceAll("[^a-z0-9-]", ""));

        // 회사 설명 추가
        position.setCompanyDescription(getCompanyDescription(company));

        // 우대사항 추가
        position.setPreferredQualifications(getPreferredQualifications(title));

        return position;
    }

    private String getCompanyDescription(String company) {
        switch (company) {
            case "카카오게임즈":
                return "카카오게임즈는 글로벌 게임 퍼블리싱 및 개발을 전문으로 하는 기업입니다. " +
                        "배틀그라운드 모바일, 검은사막 모바일 등 다양한 히트작을 보유하고 있으며, " +
                        "혁신적인 게임 경험을 제공하기 위해 최첨단 기술을 활용하고 있습니다.";
            case "Krafton":
                return "크래프톤은 배틀그라운드를 개발한 글로벌 게임 회사입니다. " +
                        "창의적이고 독창적인 게임 콘텐츠를 통해 전 세계 게이머들에게 " +
                        "새로운 재미와 경험을 선사하고 있습니다.";
            case "Pearl Abyss":
                return "펄어비스는 검은사막으로 유명한 MMORPG 전문 개발사입니다. " +
                        "고품질 그래픽과 혁신적인 게임플레이로 글로벌 시장에서 " +
                        "인정받고 있는 대한민국의 대표적인 게임 개발사입니다.";
            default:
                return company + "는 혁신적인 게임 개발과 서비스로 " +
                        "게이머들에게 최고의 경험을 제공하는 게임 회사입니다.";
        }
    }

    private String getPreferredQualifications(String title) {
        if (title.contains("웹") || title.contains("프론트엔드")) {
            return "- 게임 업계 경험자 우대\n" +
                    "- 오픈소스 기여 경험\n" +
                    "- 대용량 트래픽 처리 경험\n" +
                    "- 모바일 최적화 경험";
        } else if (title.contains("백엔드") || title.contains("서버")) {
            return "- 대규모 서비스 운영 경험\n" +
                    "- MSA 아키텍처 설계 경험\n" +
                    "- 성능 튜닝 경험\n" +
                    "- 클라우드 서비스 활용 경험";
        } else if (title.contains("AI") || title.contains("데이터")) {
            return "- 게임 데이터 분석 경험\n" +
                    "- 머신러닝 서비스 운영 경험\n" +
                    "- 논문 발표 경험\n" +
                    "- 오픈소스 기여 경험";
        } else if (title.contains("DevOps") || title.contains("인프라")) {
            return "- Kubernetes 운영 경험\n" +
                    "- 대규모 시스템 관리 경험\n" +
                    "- 보안 관련 인증 보유\n" +
                    "- 장애 대응 경험";
        } else {
            return "- 게임 업계 경험자 우대\n" +
                    "- 팀 리딩 경험\n" +
                    "- 커뮤니케이션 능력\n" +
                    "- 자기주도적 학습 능력";
        }
    }
}