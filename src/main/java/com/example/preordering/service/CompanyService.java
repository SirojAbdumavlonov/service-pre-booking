package com.example.preordering.service;

import com.example.preordering.constants.*;
import com.example.preordering.entity.*;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.CompanyFilling;
import com.example.preordering.model.JoinRequestCompany;
import com.example.preordering.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final UserAdminRepository userAdminRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ServiceRepository serviceRepository;
    private final LocationRepository locationRepository;
    private final JoinRequestRepository joinRequestRepository;

//    @Cacheable(value = "categories", key = "#categoryId")
    public Category findByCategoryId(Long categoryId){
        return categoryRepository.findByCategoryId(categoryId);
    }

    public void deleteCompany(String username){
        Company company = companyRepository.findByDirectorUsername(username);
        if(company == null){
         throw new BadRequestException("This user doesn't have a company");
        }

        List<Long> servicesId = company.getServicesId();
        if(!servicesId.isEmpty()) {
            serviceRepository.deleteServices(servicesId);
        }
//        List<String> mastersUsernames = company.getMastersUsernames();

        companyRepository.deleteCompany(username);
    }


    @Transactional
    public Company addCompany(CompanyFilling company, Long categoryId,
                              String username){
        List<String> mastersUsernames = new ArrayList<>();

        mastersUsernames.add(username);

        Category category =
                findByCategoryId(categoryId);

        Location location = Location.builder()
                .lon(company.getLon())
                .lat(company.getLat())
                .build();
        Company newCompany = Company.builder()
                .companyName(company.getCompanyName())
                .description(company.getDescription())
                .directorName(company.getDirectorName())
                .address(company.getAddress())
                .mastersUsernames(mastersUsernames)
                .location(location)
                .category(category)
                .directorUsername(username)
                .functionality(CompanyFunctionality.MULTI)
                .status(GeneralStatuses.ACTIVE)
                .category(category)
//                .companyImageName(company.getCompanyName() + "-" +
//                        multipartFile.getOriginalFilename())
                .build();

//        Image.saveImage(multipartFile, Image.COMPANY_IMAGE, company.getCompanyName());
        locationRepository.save(location);

        return companyRepository.save(newCompany);
    }
//    @Cacheable(value = "companies", key = "#categoryId")
    public List<Company> findAllCompaniesOfCategory(Long categoryId){
        return companyRepository.findByCategoryIdAndFunctionality(categoryId, CompanyFunctionality.MULTI);
    }
//    @Cacheable(value = "categories", key = "#servicesId")
    public List<String> findServicesNamesOfCompany(List<Long> servicesId){
        List<String> servicesNames =
                new ArrayList<>();
        for (Long id : servicesId){
            servicesNames.add(serviceRepository.findAllServices().get(id).getOccupationName());
        }
        return servicesNames;
    }
//    @Cacheable(value = "companies", key = "#categoryId + '_' + companyId")
    public Company findCompany(String categoryName, String directorUsername){
        return companyRepository.findByCategoryNameAndDirectorUsername(categoryName, directorUsername)
                .orElseThrow(() -> new BadRequestException("there is no such company"));
    }
    public Company findCompanyByCategoryId(Long categoryId, Long companyId){
        return companyRepository.findByCategoryIdAndCompanyId(categoryId, companyId)
                .orElseThrow(() -> new BadRequestException("there is no such company"));
    }

    public double countRate(List<String> masters){
        if(masters.isEmpty() || orderStatusRepository.getTotal(masters) == null){
            return 0.0d;
        }
        return orderStatusRepository.getTotal(masters);
    }
    public Long countSuccessfulOrders(List<String> mastersUsername){
        return orderStatusRepository.countSuccessfullOrders(OrderStatuses.FINISHED, mastersUsername);
    }
    public List<String> findUsernamesOfUserAdmins(List<Long> ids){
        return userAdminRepository.findUsernameOfUserAdmins(ids);
    }
    public List<Service> findServicesByTheirId(List<Long> ids){
        return serviceRepository.findByServiceIdInAndStatus(ids, GeneralStatuses.ACTIVE);
    }

    public List<Company> findAllCompaniesByParams(int page, int pageSize, String categoryName){

        List<Company> companies;

        Category category = categoryRepository.findByTitle(categoryName);

        Pageable recordsOnPage =
                PageRequest.of(page - 1, pageSize);

        companies = companyRepository.
                findAllByCategory_CategoryIdAndStatus(category.getCategoryId(), recordsOnPage, GeneralStatuses.ACTIVE);

        return companies;
    }
    public Company getCompanyByDirectorUsername(String username){
        return companyRepository.getCompanyByDirectorUsername(username);
    }
    public boolean ifSuchMasterExists(String username, Long companyId){
        return companyRepository.existsByMastersUsernamesIsContainingAndCompanyIdAndStatus(
                username, companyId, GeneralStatuses.ACTIVE);
    }
    public boolean ifThisDirectorEverCreated(String username){
        return companyRepository.findByDirectorUsername(username) != null;
    }
    public boolean ifThisMasterHasSoloCompany(String directorUsername){

        return companyRepository.existsByDirectorUsernameAndFunctionalityAndStatus(
                directorUsername, CompanyFunctionality.SOLO, GeneralStatuses.ACTIVE);
    }
    public boolean ifSuchServiceNameOfEmployer(String serviceName){
        return true;
    }

    public void sendRequest(String username, String companyUsername, String sender) {
        UserAdmin userAdmin = userAdminRepository.findByUsername(username);
        if(!userAdmin.getRole().equals("EMPLOYEE")){
            throw new BadRequestException("Only employee can join!");
        }
        JoinRequest joinRequest = JoinRequest.builder()
                .requestStatus(RequestStatus.REQUESTED)
                .companyUsername(companyUsername)
                .clientUsername(username)
                .sender(sender)
                .build();

        joinRequestRepository.save(joinRequest);
    }

    public List<JoinRequest> getJoinRequest(String username) {

        return joinRequestRepository.findByClientUsernameAndSender(username, Sender.COMPANY);
    }
    public List<JoinRequest> getCompanyJoinRequests(String directorUsername){
        Company company = companyRepository.findByDirectorUsername(directorUsername);

        return joinRequestRepository.findByCompanyUsernameAndSender
                (company.getCompanyUsername(),Sender.CLIENT);

    }
}
