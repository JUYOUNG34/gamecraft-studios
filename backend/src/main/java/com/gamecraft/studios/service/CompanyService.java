package com.gamecraft.studios.service;

import com.gamecraft.studios.entity.Company;
import com.gamecraft.studios.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Page<Company> searchCompanies(String name, Pageable pageable) {
        if (name != null && !name.trim().isEmpty()) {
            return companyRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
        }
        return companyRepository.findAll(pageable);
    }

    public List<Company> findCompaniesWithActiveJobs() {
        return companyRepository.findCompaniesWithActiveJobs();
    }

    // 회사 생성 또는 기존 회사 반환
    public Company findOrCreateCompany(String companyName, String logoUrl, String description) {
        Optional<Company> existing = companyRepository.findByName(companyName);
        if (existing.isPresent()) {
            return existing.get();
        }

        Company newCompany = new Company(companyName, logoUrl, description);
        return companyRepository.save(newCompany);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}