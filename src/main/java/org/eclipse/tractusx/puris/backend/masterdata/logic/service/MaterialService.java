package org.eclipse.tractusx.puris.backend.masterdata.logic.service;

import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Material;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MaterialService {

    Material create(Material material);

    Material update(Material material);

    List<Material> findAllMaterials();

    List<Material> findAllProducts();

    Material findByUuid(UUID materialUuid);

    Material findMaterialByMaterialNumberCustomer(String materialNumberCustomer);

    Material findProductByMaterialNumberCustomer(String materialNumberCustomer);

}
