package com.example.prodajaKnjigaBackend.listing.domain;

import lombok.AllArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class ListingSpecification{
    private String filter;

    public Predicate toPredicate(Root<ListingEntity> root, CriteriaBuilder criteriaBuilder){
        return null;
    }
}
