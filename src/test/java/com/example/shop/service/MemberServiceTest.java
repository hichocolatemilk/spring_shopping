package com.example.shop.service;

import com.example.shop.dto.MemberFormDto;
import com.example.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("하이");
        memberFormDto.setEmail("hi@aaa.com");
        memberFormDto.setAddress("서울");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);

        assertEquals(member.getName(), saveMember.getName());
        assertEquals(member.getEmail(), saveMember.getEmail());
        assertEquals(member.getAddress(), saveMember.getAddress());
        assertEquals(member.getPassword(), saveMember.getPassword());
        assertEquals(member.getRole(), saveMember.getRole());
    }

    @Test
    @DisplayName("중복가입테스트")
    public void saveDuplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}