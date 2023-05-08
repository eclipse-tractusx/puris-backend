package org.eclipse.tractusx.puris.backend.stock.logic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.MaterialDto;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.PartnerDto;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProductStockDto extends StockDto {

    private PartnerDto allocatedToCustomerPartner;

    public ProductStockDto(MaterialDto material, double quantity, String atSiteBpnl) {
        super(material, quantity, atSiteBpnl, new Date());
        this.setType(DT_StockTypeEnum.PRODUCT);
    }

    public ProductStockDto(MaterialDto material, double quantity, String atSiteBpnl,
                           PartnerDto allocatedToCustomerPartner) {
        super(material, quantity, atSiteBpnl, new Date());
        this.setType(DT_StockTypeEnum.PRODUCT);
        this.allocatedToCustomerPartner = allocatedToCustomerPartner;
    }


}
