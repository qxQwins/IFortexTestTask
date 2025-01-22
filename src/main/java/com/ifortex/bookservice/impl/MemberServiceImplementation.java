package com.ifortex.bookservice.impl;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberServiceImplementation implements MemberService {

    @PersistenceContext
    private EntityManager em;

    //Implement a method that finds and returns the member who has
    //read the oldest “Romance” genre book and who was most recently registered on the platform.
    @Override
    public Member findMember() {
        String query = "SELECT m FROM Member m";
        List<Member> members = em.createQuery(query, Member.class).getResultList();
        Member member = null;
        for (Member m : members) {
            for (Book b : m.getBorrowedBooks()) {
                for (String genre : b.getGenres()) {
                    if (genre.equals("Romance")) {
                        if (member == null ||
                                m.getMembershipDate().isAfter(member.getMembershipDate())) {
                            member = m;
                        }
                    }
                }
            }
        }
        return member;
    }

    //Implement a method that finds and returns
    //members who registered in 2023 but have not read any books.
    @Override
    public List<Member> findMembers() {
        String query = "SELECT m FROM Member m " +
                "where m.membershipDate between :startDate and :endDate " +
                "and m.borrowedBooks is empty ";
        List<Member> members = em.createQuery(query, Member.class)
                .setParameter("startDate", LocalDateTime.of(2023, 1, 1, 0,0,0,0))
                .setParameter("endDate", LocalDateTime.of(2024, 1, 1, 0,0,0,0))
                .getResultList();
        return members;
    }
}
