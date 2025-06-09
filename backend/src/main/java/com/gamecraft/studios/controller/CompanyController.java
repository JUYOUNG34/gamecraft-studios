package com.gamecraft.studios.controller;

import com.gamecraft.studios.entity.Company;
import com.gamecraft.studios.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {

        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Company> companies = companyService.searchCompanies(search, pageable);

            response.put("success", true);
            response.put("companies", companies.getContent());
            response.put("pagination", Map.of(
                    "currentPage", companies.getNumber(),
                    "totalPages", companies.getTotalPages(),
                    "totalElements", companies.getTotalElements(),
                    "size", companies.getSize(),
                    "hasNext", companies.hasNext(),
                    "hasPrevious", companies.hasPrevious()
            ));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "회사 목록 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCompany(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Company> company = companyService.findById(id);
            if (company.isPresent()) {
                response.put("success", true);
                response.put("company", company.get());
            } else {
                response.put("success", false);
                response.put("message", "회사를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "회사 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-jobs")
    public ResponseEntity<Map<String, Object>> getCompaniesWithJobs() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Company> companies = companyService.findCompaniesWithActiveJobs();
            response.put("success", true);
            response.put("companies", companies);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "채용 중인 회사 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
