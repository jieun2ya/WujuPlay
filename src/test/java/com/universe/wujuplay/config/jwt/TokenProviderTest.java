package com.universe.wujuplay.config.jwt;

import com.universe.wujuplay.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private MemberRepository memberRepository;


}
