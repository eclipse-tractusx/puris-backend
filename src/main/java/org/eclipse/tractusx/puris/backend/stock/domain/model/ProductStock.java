package org.eclipse.tractusx.puris.backend.stock.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;

@Entity
@Getter
@Setter
@ToString
public class ProductStock extends Stock {

    @ManyToOne
    @JoinColumn(name = "partner_uuid")
    @ToString.Exclude
    private Partner allocatedToCustomerPartner;
}