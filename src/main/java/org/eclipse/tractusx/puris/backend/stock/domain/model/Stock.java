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

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "stock_type")
@Getter
@Setter
@ToString
public class Stock {

    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "material_uuid")
    @NotNull
    private Material material;

    @NotNull
    private double quantity;

    @NotNull
    private String atSiteBpnl;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DT_StockTypeEnum type;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastUpdatedOn;

}
