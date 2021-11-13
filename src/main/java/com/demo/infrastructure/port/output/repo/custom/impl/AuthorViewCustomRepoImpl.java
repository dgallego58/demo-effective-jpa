package com.demo.infrastructure.port.output.repo.custom.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.criteria.BlazeCriteria;
import com.blazebit.persistence.criteria.BlazeCriteriaBuilder;
import com.blazebit.persistence.criteria.BlazeCriteriaQuery;
import com.blazebit.persistence.criteria.BlazeRoot;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.demo.infrastructure.port.input.dto.Filter;
import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.Author;
import com.demo.infrastructure.port.output.data.Author_;
import com.demo.infrastructure.port.output.data.Book_;
import com.demo.infrastructure.port.output.data.Convention_;
import com.demo.infrastructure.port.output.data.views.AuthorView;
import com.demo.infrastructure.port.output.repo.custom.AuthorViewCustomRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class AuthorViewCustomRepoImpl implements AuthorViewCustomRepo {

    private final CriteriaBuilderFactory criteriaBuilderFactory;
    private final EntityViewManager evm;
    @PersistenceContext
    private EntityManager entityManager;

    public AuthorViewCustomRepoImpl(CriteriaBuilderFactory criteriaBuilderFactory, EntityViewManager evm) {
        this.criteriaBuilderFactory = criteriaBuilderFactory;
        this.evm = evm;
    }

    @Override
    public List<AuthorView> findAllByFilterDTO(FilterDTO filterDTO) {
        BlazeCriteriaBuilder cb = BlazeCriteria.get(criteriaBuilderFactory);
        BlazeCriteriaQuery<Author> query = cb.createQuery(Author.class);

        BlazeRoot<Author> root = query.from(Author.class);
        query.where(predicates(root, filterDTO.getFilters(), cb));
        CriteriaBuilder<Author> criteriaBuilder = query.createCriteriaBuilder(entityManager);
        CriteriaBuilder<AuthorView> authorViewCriteriaBuilder = evm.applySetting(EntityViewSetting.create(AuthorView.class), criteriaBuilder);

        return authorViewCriteriaBuilder.getResultList();
    }

    public Predicate[] predicates(Root<Author> root, Map<Filter, Object> filters, BlazeCriteriaBuilder cb) {
        List<Predicate> predicateList = new ArrayList<>();
        filters.forEach((filter, value) -> {
            switch (filter) {
                case AUTHOR_NAME:
                    var authorNameMatch = cb.like(root.get(Author_.name), "%" + value + "%");
                    predicateList.add(authorNameMatch);
                    break;
                case BOOK_TITLE:
                    var bookTitleMatch = cb.like(root.join(Author_.books).get(Book_.title), "%" + value + "%");
                    predicateList.add(bookTitleMatch);
                    break;
                case CONVENTION_LOCATION:
                    var locationMatch = cb.like(root.join(Author_.conventions)
                            .get(Convention_.location), "%" + value + "%");
                    predicateList.add(locationMatch);
                    break;
            }
        });
        return predicateList.toArray(new Predicate[0]);
    }


}
