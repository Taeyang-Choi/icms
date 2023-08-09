package com.ogp.icms.member.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Data
public class AuthRequest {
    private String authKey;
    private String authToken;
    private String RefreshToken;
}
