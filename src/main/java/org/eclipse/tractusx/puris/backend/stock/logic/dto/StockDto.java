package org.eclipse.tractusx.puris.backend.stock.logic.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.MaterialDto;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public abstract class StockDto implements Serializable {

    private UUID uuid;
    // TODO: Check if material needs to be uuid or customerNumberMaterial instead
    private MaterialDto material;

    private double quantity;

    private String atSiteBpnl;

    private DT_StockTypeEnum type;

    private Date lastUpdatedOn;

    public StockDto(MaterialDto material, double quantity, String atSiteBpnl, Date lastUpdatedOn) {
        this.material = material;
        this.quantity = quantity;
        this.atSiteBpnl = atSiteBpnl;
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
