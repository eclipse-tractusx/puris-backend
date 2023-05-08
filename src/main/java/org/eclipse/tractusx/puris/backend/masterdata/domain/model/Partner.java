package org.eclipse.tractusx.puris.backend.masterdata.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "partner")
@Getter
@Setter
@ToString
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
    private List<Material> suppliesMaterials;

    @ManyToMany(mappedBy = "orderedByPartners")
    @ToString.Exclude
    private List<Material> ordersProducts;

    @OneToMany(mappedBy = "uuid")
    @ToString.Exclude
    private List<ProductStock> allocatedProductStocksForCustomer;

    /*
    @OneToMany(mappedBy = "owningPartner")
    @NotNull
    private List<Stock> hasStocks;
    */

}
