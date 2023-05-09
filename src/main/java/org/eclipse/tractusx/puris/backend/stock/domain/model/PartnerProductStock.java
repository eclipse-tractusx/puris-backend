package org.eclipse.tractusx.puris.backend.stock.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;

@Entity
@DiscriminatorValue("PartnerProductStock")
@Getter
@Setter
@ToString(callSuper = true)
public class PartnerProductStock extends Stock {

    @ManyToOne
    @JoinColumn(name = "supplier_partner_uuid")
    @NotNull
    private Partner supplierPartner;
}