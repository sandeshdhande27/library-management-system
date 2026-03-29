package com.sandesh.library.repository;

import com.sandesh.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    

    // Find a book by ID
    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Optional<Book> findBookById(Long bookId);

    // Update book details (only if AVAILABLE)
    @Modifying
    @Transactional
    @Query("UPDATE Book b SET " +
           "b.bookName = COALESCE(:bookName, b.bookName), " +
           "b.bookAuthor = COALESCE(:bookAuthor, b.bookAuthor), " +
           "b.bookCategory = COALESCE(:bookCategory, b.bookCategory) " +
           "WHERE b.id = :bookId AND b.bookStatus = 'AVAILABLE'")
    int updateBookDetails(Long bookId, String bookName, String bookAuthor, String bookCategory);

    @Modifying
@Transactional
@Query("DELETE FROM Book b WHERE b.id = :bookId AND b.bookStatus = 'AVAILABLE'")
int deleteBookById(Long bookId);


@Query("SELECT DISTINCT b.bookCategory FROM Book b")
List<String> findDistinctCategories();

}