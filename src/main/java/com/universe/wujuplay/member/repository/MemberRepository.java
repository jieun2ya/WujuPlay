package com.universe.wujuplay.member.repository;

import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.model.MemberResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByAccountEmail(String accountEmail);

    Optional<MemberEntity> findByProfileNickname(String profileNickname);

    Optional<MemberEntity> findByProfileNicknameAndSocialAuthId(String profileNickname, Long socialAuthId);


    Optional<MemberEntity> findBySocialAuthId(Long socialAuthId);

    Boolean existsByAccountEmail(String accountEmail);

    Optional<MemberResponse> findByPassword(String password);


}
