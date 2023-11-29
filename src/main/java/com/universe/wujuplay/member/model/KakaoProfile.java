package com.universe.wujuplay.member.model;

import lombok.Data;

@Data
public class KakaoProfile {
    public Long id;
    public String connected_at;
    public Properties properties;
    public Kakao_account kakao_account;

    @Data
    public class Properties {
        public String nickname;
    }

    @Data
    public class Kakao_account {
        public Boolean profile_nickname_needs_agreement;
        public Profile profile;

        @Data
        public class Profile {
            public String nickname;
        }
    }
}






