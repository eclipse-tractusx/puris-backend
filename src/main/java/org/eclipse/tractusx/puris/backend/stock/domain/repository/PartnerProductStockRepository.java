package org.eclipse.tractusx.puris.backend.stock.domain.repository;

import org.eclipse.tractusx.puris.backend.stock.domain.model.PartnerProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.Stock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link PartnerProductStock} that is a specific type of {@link Stock}.
 * <p>
 * PartnerProductStock has the following properties:
 * <li>stock.type == StockType.Product</li>
 * <li>stock.allocatedToMe == true</li>
 * <li>stock.allocatedToPartner == empty (no partners for yourself)</li>
 * <li>stock.material.orderedBy == set to a (specific) Partner with partner.actsAsCustomerFlag == true</li>
 */
public interface PartnerProductStockRepository extends JpaRepository<PartnerProductStock, UUID> {

    List<PartnerProductStock> findAllByType(DT_StockTypeEnum stockType);

    List<PartnerProductStock> findAllByMaterial_UuidAndType(UUID materialUuid, DT_StockTypeEnum stockType);
}
