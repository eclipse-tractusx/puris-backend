package org.eclipse.tractusx.puris.backend.stock.domain.repository;

import org.eclipse.tractusx.puris.backend.stock.domain.model.MaterialStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.Stock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link MaterialStock} that is a specific type of {@link Stock}.
 * <p>
 * MaterialStock has following properties:
 * <li>stock.type == StockType.Material</li>
 * <li>stock.allocatedToPartner == null</li>
 * <p>
 * The Material of a MaterialStock commonly
 */
public interface MaterialStockRepository extends JpaRepository<MaterialStock, UUID> {

    List<MaterialStock> findAllByType(DT_StockTypeEnum stockType);

    List<MaterialStock> findAllByMaterial_MaterialNumberCustomerAndType(String materialNumberCustomer, DT_StockTypeEnum stockType);

}
