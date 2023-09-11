package com.example.preordering.model;

import com.example.preordering.entity.Category;
import com.example.preordering.entity.Company;

import java.util.List;

public class MainPage {
    public List<Category> getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(List<Category> categoriesName) {
        this.categoriesName = categoriesName;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    List<Category> categoriesName;

    List<Company> companies;
    public MainPage(){};
    public MainPage(List<Category> categoriesName, List<Company> companies){
        this.categoriesName = categoriesName;
        this.companies = companies;
    }

}
