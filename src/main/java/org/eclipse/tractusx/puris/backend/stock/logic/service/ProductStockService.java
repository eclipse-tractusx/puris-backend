package org.eclipse.tractusx.puris.backend.stock.logic.service;

import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductStockService {

    ProductStock create(ProductStock productStock);

    ProductStock update(ProductStock productStock);

    List<ProductStock> findAll();

    ProductStock findByUuid(UUID productStockUuid);

    List<ProductStock> findAllByMaterialNumberCustomer(String materialNumberCustomer);

}
