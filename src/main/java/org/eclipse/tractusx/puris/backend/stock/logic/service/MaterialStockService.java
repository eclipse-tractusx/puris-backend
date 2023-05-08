package org.eclipse.tractusx.puris.backend.stock.logic.service;

import org.eclipse.tractusx.puris.backend.stock.domain.model.MaterialStock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MaterialStockService {

    MaterialStock create(MaterialStock materialStock);

    List<MaterialStock> findAll();

    MaterialStock findByUuid(UUID materialStockUuid);

    List<MaterialStock> findAllByMaterialNumberCustomer(String materialNumberCustomer);

    MaterialStock update(MaterialStock materialStock);

}
