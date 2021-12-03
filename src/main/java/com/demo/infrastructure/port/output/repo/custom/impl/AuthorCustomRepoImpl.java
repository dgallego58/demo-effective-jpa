package com.demo.infrastructure.port.output.repo.custom.impl;

import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.Author;
import com.demo.infrastructure.port.output.data.Author_;
import com.demo.infrastructure.port.output.data.Book;
import com.demo.infrastructure.port.output.data.Book_;
import com.demo.infrastructure.port.output.data.Convention;
import com.demo.infrastructure.port.output.data.Convention_;
import com.demo.infrastructure.port.output.repo.custom.AuthorCustomRepo;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class AuthorCustomRepoImpl implements AuthorCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    //N + 1 and fetch problem when try to fetch entities of the same relationship
    @Override
    //@Transactional(readOnly = true) no transactional to execute n+1
    public List<Author> authorNPlus1(FilterDTO filterDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = criteriaBuilder.createQuery(Author.class);
        Root<Author> author = query.from(Author.class);
        this.<Author, Book>doFetch(author, Author_.BOOKS); //fetch to avoid lazy init over M-To-M fetch
        this.<Author, Convention>doFetch(author, Author_.CONVENTIONS); //also, over this one
        CriteriaQuery<Author> completeQuery = query.select(author)
                .where(this.<Author, Book, Convention>predicates(filterDTO, author, criteriaBuilder))
                .orderBy(criteriaBuilder.asc(author.get(Author_.name)));

        return entityManager.createQuery(completeQuery)
                .setFirstResult(filterDTO.getOffset())//page*size
                .setMaxResults(filterDTO.getLimit())
                .getResultList();
    }

    public <E, J> Join<E, J> join(Root<E> parentEntity, String entityAttribute) {
        return parentEntity.join(entityAttribute, JoinType.LEFT);
    }

    public <E, J> void doFetch(Root<E> parentEntity, String entityAttribute) {
        parentEntity.<E, J>fetch(entityAttribute, JoinType.LEFT);
    }


    public <E, B, C> Predicate[] predicates(FilterDTO filterDTO, Root<E> authorRoot, CriteriaBuilder cb) {
        Join<E, B> books = join(authorRoot, "books");
        Join<E, C> conventions = join(authorRoot, "conventions");
        List<Predicate> predicateList = new ArrayList<>();
        filterDTO.getFilters()
                .forEach((filter, value) -> {
                    switch (filter) {
                        case AUTHOR_NAME:
                            Predicate likeAuthorName = cb.like(authorRoot.get(filter.getKeyword()), "%" + value + "%");
                            predicateList.add(likeAuthorName);
                            break;
                        case BOOK_TITLE:
                            Predicate likeBookName = cb.like(books.get(filter.getKeyword()), "%" + value + "%");
                            predicateList.add(likeBookName);
                            break;
                        case CONVENTION_LOCATION:
                            Predicate likeConventionLocation = cb.like(conventions.get(filter.getKeyword()), "%" + value + "%");
                            predicateList.add(likeConventionLocation);
                            break;
                    }
                });
        return predicateList.toArray(new Predicate[0]);
    }

    // MULTI-FETCH en un PAGINADO
    @Override
    public List<Author> authorsMultiFetch(FilterDTO filterDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> author = query.from(Author.class);

        this.<Author, Book>doFetch(author, "books"); //we need to do a fetch to execute fetch over the relationships
        this.<Author, Convention>doFetch(author, "conventions"); // Also, we specified the join type to execute over the query

        CriteriaQuery<Author> authorCriteriaQuery = query
                .select(author)
                .distinct(true)
                .where(predicates(filterDTO, author, cb))
                .orderBy(cb.asc(author.get(Author_.name)));
        return entityManager.createQuery(authorCriteriaQuery)
                .setFirstResult(filterDTO.getOffset())
                .setMaxResults(filterDTO.getLimit())
                .getResultList();
    }

    // PARTITION QUERY
    @Override
    public List<Author> authorByPartition(FilterDTO filterDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UUID> query = cb.createQuery(UUID.class);
        Root<Author> authorToFetchIds = query.from(Author.class);

        Predicate[] predicates = this.<Author, Book, Convention>predicates(filterDTO, authorToFetchIds, cb);
        CriteriaQuery<UUID> queryToGetIds = query.select(authorToFetchIds.get(Author_.ID))
                .where(predicates)
                .orderBy(cb.asc(authorToFetchIds.get(Author_.name)));
        var authorIds = entityManager.createQuery(queryToGetIds)
                .setFirstResult(filterDTO.getOffset())
                .setMaxResults(filterDTO.getLimit())//se usa paginado en la primera query evitando así el resize en memoria
                .getResultList();

        CriteriaQuery<Author> queryToGetAuthors = cb.createQuery(Author.class);
        Root<Author> authorRoot = queryToGetAuthors.from(Author.class);

        this.<Author, Book>doFetch(authorRoot, Author_.BOOKS);
        this.<Author, Convention>doFetch(authorRoot, Author_.CONVENTIONS);
        CriteriaQuery<Author> finalQuery = queryToGetAuthors
                .select(authorRoot)
                .distinct(true)
                .where(authorRoot.get(Author_.id).in(authorIds))
                .orderBy(cb.asc(authorRoot.get(Author_.name)));

        return entityManager.createQuery(finalQuery)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false) //discarding duplicated java object to optimize memory
                .getResultList();
    }


    @Override
    public List<Author> fetchCast(FilterDTO filterDTO) {
        var cb = entityManager.getCriteriaBuilder();
        var cq = cb.createQuery(Author.class);
        var root = cq.from(Author.class);
        var books = (Join<Author, Book>) root.fetch(Author_.books, JoinType.LEFT);
        var conventions = (Join<Author, Convention>) root.fetch(Author_.conventions, JoinType.LEFT);

        Predicate[] predicates = this.predicates(filterDTO, root, cb);
        cq.distinct(true)
                .where(predicates)
                .orderBy(cb.asc(books.get(Book_.title)), cb.desc(conventions.get(Convention_.location)));

        return entityManager.createQuery(cq).setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false).getResultList();

    }

}
