package com.example.prodajaKnjigaBackend.listing;

import com.example.prodajaKnjigaBackend.author.domain.AuthorEntity;
import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.LinkedList;

@AllArgsConstructor
public class ListingFilter implements Specification<ListingEntity> {

    private final String filter;
    private final Boolean fav;
    private final Long userId;

    @Override
    public Predicate toPredicate(Root<ListingEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        query.distinct(true);
        var listPredicates = new LinkedList<Predicate>();

        if (StringUtils.hasLength(filter)) {
            Join<ListingEntity, BookEntity> booksJoin = root.join("books");
            Join<BookEntity, AuthorEntity> authorsJoin = booksJoin.join("authors");

            String filterLower = "%" + filter.toLowerCase() + "%";

            Expression<String> fullName = criteriaBuilder.concat(
                    criteriaBuilder.concat(criteriaBuilder.lower(authorsJoin.get("firstname")), " "),
                    criteriaBuilder.lower(authorsJoin.get("lastname"))
            );

            Expression<String> reverseFullName = criteriaBuilder.concat(
                    criteriaBuilder.concat(criteriaBuilder.lower(authorsJoin.get("lastname")), " "),
                    criteriaBuilder.lower(authorsJoin.get("firstname"))
            );

            var filterPredicate = new LinkedList<Predicate>();

            filterPredicate.add(
                    criteriaBuilder.like(fullName, filterLower)
            );

            filterPredicate.add(
                    criteriaBuilder.like(reverseFullName, filterLower)
            );

            filterPredicate.add(
                    criteriaBuilder.like(criteriaBuilder.lower(booksJoin.get("title")), filterLower)
            );

            filterPredicate.add(
                    criteriaBuilder.like(criteriaBuilder.lower(booksJoin.get("publisher")), filterLower)
            );

            listPredicates.add(criteriaBuilder.or(filterPredicate.toArray(Predicate[]::new)));
        }

        if (Boolean.TRUE.equals(fav) && userId != null) {
            var favJoin = root.join("favoriteEntities");
            listPredicates.add(criteriaBuilder.equal(favJoin.get("user").get("id"), userId));
        }

        if (listPredicates.isEmpty()) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.and(listPredicates.toArray(Predicate[]::new));
    }
}
