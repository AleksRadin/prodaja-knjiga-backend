package com.example.prodajaKnjigaBackend.listing.service;

import com.example.prodajaKnjigaBackend.author.domain.AuthorEntity;
import com.example.prodajaKnjigaBackend.author.domain.AuthorRepository;
import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import com.example.prodajaKnjigaBackend.book.domain.BookRepository;
import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingDTO;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingRequest;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingUpdate;
import com.example.prodajaKnjigaBackend.listing.ListingFilter;
import com.example.prodajaKnjigaBackend.listing.ListingMapper;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.example.prodajaKnjigaBackend.listing.domain.ListingRepository;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {
    private final ListingRepository listingRepository;
    private final BookRepository bookRepository;
    private final SecurityUtils securityUtils;
    private final AuthorRepository authorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ListingDTO createListing(ListingRequest request) {
        UserEntity user = securityUtils.getAuthenticatedUser();
        Set<BookEntity> bookEntities = request.getBooks().stream()
                .map(bookData -> {
                    Set<AuthorEntity> authors = bookData.getAuthors().stream()
                            .map(authorDto -> {
                                if (authorDto.getId() != null && authorDto.getId() > 0) {
                                    return authorRepository.findById(authorDto.getId())
                                            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
                                }
                                return authorRepository.findByFirstnameAndLastname(authorDto.getFirstname(), authorDto.getLastname())
                                        .orElseGet(() -> {
                                            AuthorEntity newAuthor = new AuthorEntity();
                                            newAuthor.setFirstname(authorDto.getFirstname());
                                            newAuthor.setLastname(authorDto.getLastname());
                                            return authorRepository.save(newAuthor);
                                        });
                            })
                            .collect(Collectors.toSet());

                    return bookRepository.findByTitleAndPublisher(bookData.getTitle(), bookData.getPublisher())
                            .stream()
                            .filter(existingBook -> existingBook.getAuthors().equals(authors))
                            .findFirst()
                            .orElseGet(() -> {
                                BookEntity newBook = new BookEntity();
                                newBook.setTitle(bookData.getTitle());
                                newBook.setPublisher(bookData.getPublisher());
                                newBook.setAuthors(authors);
                                return bookRepository.save(newBook);
                            });
                })
                .collect(Collectors.toSet());


        ListingEntity listingEntity = new ListingEntity();
        listingEntity.setUser(user);
        listingEntity.setBooks(bookEntities);
        listingEntity.setPrice(request.getPrice());
        listingEntity.setCondition(request.getCondition());
        listingEntity.setDescription(request.getDescription());
        listingEntity.setCreatedAt(LocalDate.now());

        return ListingMapper.toDto(listingRepository.save(listingEntity));
    }

    @Override
    public Page<ListingDTO> getAllListings(String filter, Boolean isfavorite, Long userId, Pageable pageable) {
        var specification = new ListingFilter(filter, isfavorite, userId);
        return listingRepository.findAll(specification, pageable).map(ListingMapper::toDto);
    }


    @Transactional
    @Override
    public void deleteListing(Long listingId){
        UserEntity currentUser = securityUtils.getAuthenticatedUser();

        ListingEntity listingEntity = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("ListingEntity not found with id: " + listingId));

        boolean isAdmin = securityUtils.hasRole("ADMIN");
        boolean isOwner = listingEntity.getUser().getEmail().equals(currentUser.getEmail());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Only owner or admin can delete listing");
        }

        try {
            listingRepository.deleteListingBooks(listingId);
            listingRepository.deleteListingFavorites(listingId);

            if (entityManager != null) {
                entityManager.flush();
                entityManager.clear();
            }

            listingRepository.deleteById(listingId);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public ListingDTO updateListing(Long listingId, ListingUpdate listingUpdate){
        UserEntity currentUser = securityUtils.getAuthenticatedUser();

        ListingEntity listingEntity = listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("ListingEntity not found with ID: " + listingId));

        boolean isAdmin = securityUtils.hasRole("ADMIN");
        boolean isOwner = listingEntity.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Only owner or admin can change listing");
        }
          Set<BookEntity> updatedBookEntities = listingUpdate.getBooks().stream()
                .map(bookData -> {
                    Set<AuthorEntity> authors = bookData.getAuthors().stream()
                            .map(authorDto -> {
                                if (authorDto.getId() != null && authorDto.getId() > 0) {
                                    return authorRepository.findById(authorDto.getId())
                                            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
                                }
                                return authorRepository.findByFirstnameAndLastname(authorDto.getFirstname(), authorDto.getLastname())
                                        .orElseGet(() -> {
                                            AuthorEntity newAuthor = new AuthorEntity();
                                            newAuthor.setFirstname(authorDto.getFirstname());
                                            newAuthor.setLastname(authorDto.getLastname());
                                            return authorRepository.save(newAuthor);
                                        });
                            })
                            .collect(Collectors.toSet());

                    return bookRepository.findByTitleAndPublisher(bookData.getTitle(), bookData.getPublisher())
                            .stream()
                            .filter(existingBook -> existingBook.getAuthors().equals(authors))
                            .findFirst()
                            .orElseGet(() -> {
                                BookEntity newBook = new BookEntity();
                                newBook.setTitle(bookData.getTitle());
                                newBook.setPublisher(bookData.getPublisher());
                                newBook.setAuthors(authors);
                                return bookRepository.save(newBook);
                            });
                })
                .collect(Collectors.toSet());

        listingEntity.setBooks(updatedBookEntities);
        listingEntity.setPrice(listingUpdate.getPrice());
        listingEntity.setCondition(listingUpdate.getCondition());
        listingEntity.setDescription(listingUpdate.getDescription());

        return ListingMapper.toDto(listingRepository.save(listingEntity));
    }



}
