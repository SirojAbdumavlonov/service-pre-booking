package com.example.preordering.service;

import com.example.preordering.entity.Category;
import com.example.preordering.entity.Company;
import com.example.preordering.entity.Service;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.CompanyFilling;
import com.example.preordering.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final UserAdminRepository userAdminRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ServiceRepository serviceRepository;
    @Transactional
    public Company addCompany(CompanyFilling company, Long categoryId, String username){
        Set<Long> servicesId = new HashSet<>();
        Set<Long> mastersId = new HashSet<>();

        Category category =
                categoryRepository.findByCategoryId(categoryId);

        Company newCompany = Company.builder()
                .companyName(company.getCompanyName())
                .description(company.getDescription())
                .directorName(company.getDirectorName())
                .address(company.getLocation())
                .servicesId(servicesId)
                .mastersId(mastersId)
                .category(category)
                .directorUsername(username)
                .build();

        return companyRepository.save(newCompany);
    }
    public List<Company> findAllCompaniesOfCategory(Long categoryId){
        return companyRepository.findByCategoryId(categoryId);
    }
    public Company findCompany(Long categoryId, Long companyId){
        return companyRepository.findByCategoryIdAndCompanyId(categoryId,companyId)
                .orElseThrow(() -> new BadRequestException("there is no such company"));
    }
    public Long countRate(Set<Long> masters){
        Long likes = orderStatusRepository.countLikes(masters);
        Long dislikes = orderStatusRepository.countDislikes(masters);
        if(likes == 0 && dislikes == 0){
            return likes;
        }
        return (likes/(likes + dislikes))*100;
    }
    public Set<Long> findMastersOfCompany(String directorUsername, Set<Long> masters){
        Long companyDirectorId = userAdminRepository.findUserAdminIdByUsername(directorUsername);
        masters.add(companyDirectorId);
        return masters;
    }
    public Long countSuccessfulOrders(Set<Long> mastersId){
        return orderStatusRepository.countSuccessfullOrders("finished", mastersId);
    }
    public Set<String> findUsernamesOfUserAdmins(Set<Long> ids){
        return userAdminRepository.findUsernameOfUserAdmins(ids);
    }
    public Set<Service> findServicesByTheirId(Set<Long> ids){
        return serviceRepository.findByServiceIdIn(ids);
    }

}
