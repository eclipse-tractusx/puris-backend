package org.eclipse.tractusx.puris.backend.masterdata.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.eclipse.tractusx.puris.backend.stock.domain.model.PartnerProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "partner")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Partner {

    @Id
    @GeneratedValue
    private UUID uuid;
    private String name;
    @NotNull
    private boolean actsAsCustomerFlag;
    @NotNull
    private boolean actsAsSupplierFlag;
    private String edcUrl;
    private String bpnl;
    private String siteBpns;

    @ManyToMany(mappedBy = "suppliedByPartners")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Set<Material> suppliesMaterials;
    ;

    @ManyToMany(mappedBy = "orderedByPartners")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Set<Material> ordersProducts;
    ;

    @OneToMany(mappedBy = "uuid")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private List<ProductStock> allocatedProductStocksForCustomer;

    @OneToMany(mappedBy = "uuid")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private List<PartnerProductStock> partnerProductStocks;
}
