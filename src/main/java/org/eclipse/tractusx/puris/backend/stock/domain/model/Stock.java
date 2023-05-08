package org.eclipse.tractusx.puris.backend.stock.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Material;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class Stock {
    //Todo Check if DiscriminatorColumn for type is better way to differentiate between Product and Material Stock than Stock.type https://www.baeldung.com/hibernate-inheritance
    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "material_uuid")
    @NotNull
    @ToString.Exclude
    private Material material;

    @NotNull
    private double quantity;

    // TODO: do we need the stock.siteBpns?
    /*
    @ManyToOne
    @JoinColumn(name = "partner_uuid")
    @NotNull
    private Partner owningPartner;
    */
    @NotNull
    private String atSiteBpnl;


    @Enumerated(EnumType.STRING)
    @NotNull
    private DT_StockTypeEnum type;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastUpdatedOn;

}
