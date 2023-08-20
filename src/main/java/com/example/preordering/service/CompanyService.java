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
import org.springframework.cache.annotation.Cacheable;
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
    private final ServicesService servicesService;

//    @Cacheable(value = "categories", key = "#categoryId")
    public Category findByCategoryId(Long categoryId){
        return categoryRepository.findByCategoryId(categoryId);
    }

    @Transactional
    public Company addCompany(CompanyFilling company, Long categoryId,
                              String username){
        List<Long> mastersId = new ArrayList<>();

        Category category =
                findByCategoryId(categoryId);

        Company newCompany = Company.builder()
                .companyName(company.getCompanyName())
                .description(company.getDescription())
                .directorName(company.getDirectorName())
                .address(company.getLocation())
                .mastersId(mastersId)
                .category(category)
                .directorUsername(username)
//                .companyImageName(company.getCompanyName() + "-" +
//                        multipartFile.getOriginalFilename())
                .build();
//        Image.saveImage(multipartFile, Image.COMPANY_IMAGE, company.getCompanyName());
        return companyRepository.save(newCompany);
    }
//    @Cacheable(value = "companies", key = "#categoryId")
    public List<Company> findAllCompaniesOfCategory(Long categoryId){
        return companyRepository.findByCategoryId(categoryId);
    }
//    @Cacheable(value = "categories", key = "#servicesId")
    public List<String> findServicesNamesOfCompany(List<Long> servicesId){
        List<String> servicesNames =
                new ArrayList<>();
        for (Long id : servicesId){
            servicesNames.add(servicesService.getServiceById(id).getOccupationName());
        }
        return servicesNames;
    }
//    @Cacheable(value = "companies", key = "#categoryId + '_' + companyId")
    public Company findCompany(Long categoryId, Long companyId){
        return companyRepository.findByCategoryIdAndCompanyId(categoryId,companyId)
                .orElseThrow(() -> new BadRequestException("there is no such company"));
    }

    public double countRate(List<Long> masters){
        if(masters.isEmpty() || orderStatusRepository.getTotal(masters) == null){
            return 0.0d;
        }
        return orderStatusRepository.getTotal(masters);
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
