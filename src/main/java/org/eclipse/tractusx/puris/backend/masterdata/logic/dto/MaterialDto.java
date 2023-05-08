package org.eclipse.tractusx.puris.backend.masterdata.logic.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MaterialDto implements Serializable {

    private UUID uuid;

    private List<PartnerDto> suppliedByPartners;

    private List<PartnerDto> orderedByPartners;

    private List<MaterialDto> materialOnStocks;

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

    public MaterialDto(boolean materialFlag, boolean productFlag, String materialNumberCustomer, String materialNumberSupplier, String materialNumberCx, String name) {
        this.materialFlag = materialFlag;
        this.productFlag = productFlag;
        this.materialNumberCustomer = materialNumberCustomer;
        this.materialNumberSupplier = materialNumberSupplier;
        this.materialNumberCx = materialNumberCx;
        this.name = name;
    }

}
