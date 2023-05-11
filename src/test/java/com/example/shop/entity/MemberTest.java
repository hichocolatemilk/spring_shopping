package com.example.shop.entity;

import com.example.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "AA", roles = "USER")
    public void auditingTest(){
        Member newmember = new Member();
        memberRepository.save(newmember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newmember.getId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("reg time:" + member.getRegTime());
        System.out.println("update time:" + member.getUpdateTime());
        System.out.println("create time:" + member.getCreatedBy());
        System.out.println("mod time:" + member.getModifiedBy());

    }

}