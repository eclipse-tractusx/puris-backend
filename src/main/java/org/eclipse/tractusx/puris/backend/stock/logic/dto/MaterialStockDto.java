package org.eclipse.tractusx.puris.backend.stock.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.MaterialDto;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class MaterialStockDto extends StockDto {

    public MaterialStockDto(MaterialDto material, double quantity, String atSiteBpnl) {
        super(material, quantity, atSiteBpnl, new Date());
        this.setType(DT_StockTypeEnum.MATERIAL);
    }
}
