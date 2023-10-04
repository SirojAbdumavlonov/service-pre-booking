package com.example.preordering.model;

import com.example.preordering.entity.Category;
import com.example.preordering.entity.Company;

import java.util.List;

public class MainPage<T> {
    public List<Category> getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(List<Category> categoriesName) {
        this.categoriesName = categoriesName;
    }

    public List<T> getCompanies() {
        return companies;
    }

    public void setCompanies(List<T> companies) {
        this.companies = companies;
    }

    List<Category> categoriesName;

    List<T> companies;
    public MainPage(){};
    public MainPage(List<Category> categoriesName, List<T> companies){
        this.categoriesName = categoriesName;
        this.companies = companies;
    }

}
