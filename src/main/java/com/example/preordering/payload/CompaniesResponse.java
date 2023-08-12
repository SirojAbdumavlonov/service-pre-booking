package com.example.preordering.payload;

import java.util.List;

public record CompaniesResponse(String companyName,
                                List<String> servicesNames,
                                String companyImageName,
                                Long companyId,
                                Long categoryId) {
}
