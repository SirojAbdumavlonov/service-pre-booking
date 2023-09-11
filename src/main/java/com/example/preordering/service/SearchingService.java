package com.example.preordering.service;

import com.example.preordering.entity.UserAdmin;
import com.example.preordering.model.EmployeeSearched;
import com.example.preordering.repository.UserAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchingService {
    private final UserAdminRepository userAdminRepository;

    public List<Object> findByParameters(int searchSize, String category,
                                         String searchType, String searchedObjectName){

        if(searchType.equals("masters")){
            List<EmployeeSearched> employeeSearchedList = new ArrayList<>();
            Pageable recordsOnPage =
                    PageRequest.of(0, searchSize);

            for (UserAdmin userAdmin: userAdminRepository.
                    findByUsernameOfEmployee(searchedObjectName, recordsOnPage)){
                employeeSearchedList.add(
                        new EmployeeSearched(userAdmin.getUsername(),
                                userAdmin.getFirstName() + userAdmin.getLastName(),
                                userAdmin.getUserAdminImageName())
                );
            }
            return Collections.singletonList(employeeSearchedList);
        }
        else if (searchType.equals("service")) {
            return null;
        }
        return null;
    }


}
