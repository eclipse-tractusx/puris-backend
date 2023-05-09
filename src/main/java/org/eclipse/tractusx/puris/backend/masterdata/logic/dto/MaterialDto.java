package org.eclipse.tractusx.puris.backend.masterdata.logic.dto;

import lombok.*;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.StockDto;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class MaterialDto implements Serializable {

    private UUID uuid;

    @Setter(AccessLevel.NONE)
    private Set<PartnerDto> suppliedByPartners = new HashSet<>();

    @Setter(AccessLevel.NONE)
    private Set<PartnerDto> orderedByPartners = new HashSet<>();

    @Setter(AccessLevel.NONE)
    private List<StockDto> materialOnStocks = new ArrayList<>();

    /**
     * If true, then the Material is a material (input for production / something I buy).
     * <p>
     * Boolean because there could be companies (tradesmen company) that buy and sell the same material.
     */
    private boolean materialFlag;

    /**
     * If true, then the Material is a product (output of production / something I sell).
     * <p>
     * Boolean because there could be companies (tradesmen company) that buy and sell the same material.
     */
    private boolean productFlag;

    private String materialNumberCustomer;

    private String materialNumberSupplier;

    private String materialNumberCx;

    private String name;

    public MaterialDto(boolean materialFlag, boolean productFlag, String materialNumberCustomer, String materialNumberSupplier, String materialNumberCx, String name) {
        super();
        this.materialFlag = materialFlag;
        this.productFlag = productFlag;
        this.materialNumberCustomer = materialNumberCustomer;
        this.materialNumberSupplier = materialNumberSupplier;
        this.materialNumberCx = materialNumberCx;
        this.name = name;
    }

    public void addSuppliedByPartner(PartnerDto supplierPartner) {
        this.suppliedByPartners.add(supplierPartner);
        supplierPartner.getSuppliesMaterials().add(this);
    }

    public void addOrderedByPartner(PartnerDto customerPartner) {
        this.orderedByPartners.add(customerPartner);
        customerPartner.getOrdersProducts().add(this);
    }

}
