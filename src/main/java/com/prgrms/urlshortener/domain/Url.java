package com.prgrms.urlshortener.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.prgrms.urlshortener.exception.InvalidShortedUrlException;
import com.prgrms.urlshortener.exception.InvalidUrlException;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Url {

    private static final String URL_REGEX = "^(https?:\\/\\/)?([^.][\\da-z\\.-]+\\.[a-z\\.]{2,6}|[\\d\\.]+)([\\/:?=&#]{1}[\\da-z\\.-]+)*[\\/\\?]?$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originUrl;

    @Embedded
    private ShortedUrl shortedUrl;

    @Column(nullable = false)
    private long requestCount = 0L;

    @CreatedDate
    private LocalDateTime createdDateTime;

    protected Url() {
    }

    public Url(String originUrl) {
        this(null, originUrl);
    }

    public Url(Long id, String originUrl) {
        this(id, originUrl, null);
    }

    public Url(Long id, String originUrl, ShortedUrl shortedUrl) {
        validateUrl(originUrl);
        this.id = id;
        this.originUrl = convertUrl(originUrl);
        this.shortedUrl = shortedUrl;
    }

    private String convertUrl(String originUrl) {
        if (!originUrl.startsWith("http")) {
            return "http://" + originUrl;
        }
        return originUrl;
    }

    private void validateUrl(String url) {
        if (!URL_PATTERN.matcher(url).matches()) {
            throw new InvalidUrlException(url);
        }
    }

    public void assignShortedUrl(ShortedUrl shortedUrl) {
        validateAssigned();
        this.shortedUrl = shortedUrl;
    }

    public void addRequestCount() {
        this.requestCount++;
    }

    private void validateAssigned() {
        if (Objects.nonNull(this.shortedUrl)) {
            throw new InvalidShortedUrlException("이미 단축 URL이 할당되어 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public ShortedUrl getShortedUrl() {
        return shortedUrl;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }
}
