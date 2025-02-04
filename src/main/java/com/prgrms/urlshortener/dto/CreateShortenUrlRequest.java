package com.prgrms.urlshortener.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.prgrms.urlshortener.common.validator.UrlFormat;

public class CreateShortenUrlRequest {

    @UrlFormat(message = "잘못된 URL 포맷입니다.")
    @NotNull(message = "URL을 입력해주세요.")
    @Length(max = 255, message = "길이가 {max}이하의 URL만 가능합니다.")
    private String originUrl;

    @NotNull(message = "인코딩 타입을 입력해주세요.")
    private String encodedType;

    public CreateShortenUrlRequest(String originUrl, String encodedType) {
        this.originUrl = originUrl;
        this.encodedType = encodedType;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public String getEncodedType() {
        return encodedType;
    }

}
