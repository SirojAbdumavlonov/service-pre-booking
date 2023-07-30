package com.example.preordering.service;

import com.example.preordering.constants.OrderStatuses;
import com.example.preordering.entity.Category;
import com.example.preordering.entity.Company;
import com.example.preordering.entity.Service;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.CompanyFilling;
import com.example.preordering.repository.*;
import com.example.preordering.utils.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public Company addCompany(CompanyFilling company, Long categoryId,
                              String username, MultipartFile multipartFile){
        List<Long> servicesId = new ArrayList<>();
        List<Long> mastersId = new ArrayList<>();

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
                .companyImageName(company.getCompanyName() + "-" +
                        multipartFile.getOriginalFilename())
                .build();
        Image.saveImage(multipartFile, Image.COMPANY_IMAGE, company.getCompanyName());
        return companyRepository.save(newCompany);
    }
    public List<Company> findAllCompaniesOfCategory(Long categoryId){
        return companyRepository.findByCategoryId(categoryId);
    }
    public Company findCompany(Long categoryId, Long companyId){
        return companyRepository.findByCategoryIdAndCompanyId(categoryId,companyId)
                .orElseThrow(() -> new BadRequestException("there is no such company"));
    }
    public Long countRate(List<Long> masters){
        Long likes = orderStatusRepository.countLikes(masters);
        Long dislikes = orderStatusRepository.countDislikes(masters);
        if(likes == 0 && dislikes == 0){
            return likes;
        }
        return (likes/(likes + dislikes))*100;
    }
    public List<Long> findMastersOfCompany(String directorUsername, List<Long> masters){
        Long companyDirectorId = userAdminRepository.findUserAdminIdByUsername(directorUsername);
        masters.add(companyDirectorId);
        return masters;
    }
    public Long countSuccessfulOrders(List<Long> mastersId){
        return orderStatusRepository.countSuccessfullOrders(OrderStatuses.FINISHED, mastersId);
    }
    public List<String> findUsernamesOfUserAdmins(List<Long> ids){
        return userAdminRepository.findUsernameOfUserAdmins(ids);
    }
    public List<Service> findServicesByTheirId(List<Long> ids){
        return serviceRepository.findByServiceIdIn(ids);
    }

}
