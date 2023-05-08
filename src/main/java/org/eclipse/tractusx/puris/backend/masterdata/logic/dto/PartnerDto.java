package org.eclipse.tractusx.puris.backend.masterdata.logic.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PartnerDto implements Serializable {

    private String name;

    private boolean actsAsCustomerFlag;
    private boolean actsAsSupplierFlag;

    private String edcUrl;
    private String bpnl;
    private String siteBpns;

    @Nullable
    @ToString.Exclude
    private List<MaterialDto> suppliesMaterials;

    @Nullable
    @ToString.Exclude
    private List<MaterialDto> ordersProducts;

    public PartnerDto(String name, boolean actsAsCustomerFlag, boolean actsAsSupplierFlag, String edcUrl, String bpnl, String siteBpns) {
        this.name = name;
        this.actsAsCustomerFlag = actsAsCustomerFlag;
        this.actsAsSupplierFlag = actsAsSupplierFlag;
        this.edcUrl = edcUrl;
        this.bpnl = bpnl;
        this.siteBpns = siteBpns;
    }
}
