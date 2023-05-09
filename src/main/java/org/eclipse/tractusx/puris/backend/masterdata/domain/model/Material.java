package org.eclipse.tractusx.puris.backend.masterdata.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.eclipse.tractusx.puris.backend.stock.domain.model.Stock;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "material")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Material {

    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToMany
    @JoinTable(
            name = "partner_supplies_product",
            joinColumns = @JoinColumn(name = "material_uuid"),
            inverseJoinColumns = @JoinColumn(name = "partner_uuid")
    )
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Set<Partner> suppliedByPartners;
    ;

    @ManyToMany
    @JoinTable(
            name = "partner_orders_product",
            joinColumns = @JoinColumn(name = "material_uuid"),
            inverseJoinColumns = @JoinColumn(name = "partner_uuid")
    )
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Set<Partner> orderedByPartners;

    @OneToMany(mappedBy = "uuid")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private List<Stock> materialOnStocks;

    /**
     * If true, then the Material is a material (input for production / something I buy).
     * <p>
     * Boolean because there could be companies (trademen company) that buy and sell the same material.
     */
    private boolean materialFlag;

    /**
     * If true, then the Material is a product (output of production / something I sell.
     * <p>
     * Boolean because there could be companies (trademen company) that buy and sell the same material.
     */
    private boolean productFlag;

    private String materialNumberCustomer;

    private String materialNumberSupplier;

    private String materialNumberCx;

    private String name;

}
