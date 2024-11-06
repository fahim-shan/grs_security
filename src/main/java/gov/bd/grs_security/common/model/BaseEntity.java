package gov.bd.grs_security.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BaseEntity {
    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "modified_at")
    private Date updatedAt;

    @JsonIgnore
    @Column(name = "created_by")
    private Long createdBy;

    @JsonIgnore
    @Column(name = "modified_by")
    private Long modifiedBy;

    @JsonIgnore
    @Column(name = "status")
    private Boolean status;
}
