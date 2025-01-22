package com.ifortex.bookservice.impl;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@EnableTransactionManagement
public class BookServiceImplementation implements BookService {
    @PersistenceContext
    private EntityManager em;

    //Implement a method that retrieves the total count of books for each genre,
    //ordered from the genre with the most books to the least.
    @Override
    public Map<String, Long> getBooks() {
        String query = "SELECT b FROM Book b";
        List<Book> booksList = em.createQuery(query, Book.class).getResultList();
        Map<String, Long> booksMap = new HashMap<>();
        for (Book book : booksList) {
            for(String genre : book.getGenres())
            {
                if(booksMap.containsKey(genre))
                    booksMap.put(genre, booksMap.get(genre) + 1);
                else
                    booksMap.put(genre, 1L);
            }
        }
        TreeMap<String, Long> sortedBooksMap = new TreeMap<>(
                (k1, k2) -> {
                    int valueCompare = booksMap.get(k2).compareTo(booksMap.get(k1));
                    return valueCompare != 0 ? valueCompare : k1.compareTo(k2);
                }
        );
        sortedBooksMap.putAll(booksMap);
        return sortedBooksMap;
    }
}
