package org.eclipse.tractusx.puris.backend.stock.domain.repository;

import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.Stock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link ProductStock} that is a specific type of {@link Stock}.
 * <p>
 * ProductStock has following properties:
 * <li>stock.type == StockType.Product</li>
 * <li>stock.allocatedToPartner == set to a partner with partner.actsAsCustomerFlag and who has a relation partner.ordersMaterial == productStock.material</li>
 * <li>stock.material.orderedBy == set to a (specific) Partner with partner.actsAsCustomerFlag == true</li>
 */
public interface ProductStockRepository extends JpaRepository<ProductStock, UUID> {

    List<ProductStock> findAllByTypeAndUuid(DT_StockTypeEnum stockType, UUID productStockUuid);

    List<ProductStock> findAllByType(DT_StockTypeEnum stockType);

    List<ProductStock> findAllByMaterial_MaterialNumberCustomerAndType(String materialNumberCustomer, DT_StockTypeEnum stockType);
}
