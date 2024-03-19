package com.kc.poc.drools.proposition;

import com.fasterxml.jackson.annotation.JsonView;
import com.kc.poc.drools.BaseEntity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PropositionElement extends BaseEntity {

    private static final long serialVersionUID = 8020682326715898525L;

    @JsonView(JsonViews.class)
    private Long referenceUid;

    public Long getReferenceUid() {
        return referenceUid;
    }

    public void setReferenceUid(Long referenceUid) {
        this.referenceUid = referenceUid;
    }

}
