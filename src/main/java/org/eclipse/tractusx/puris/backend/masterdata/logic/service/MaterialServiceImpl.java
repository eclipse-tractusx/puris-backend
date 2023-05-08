package org.eclipse.tractusx.puris.backend.masterdata.logic.service;

import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Material;
import org.eclipse.tractusx.puris.backend.masterdata.domain.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    MaterialRepository materialRepository;

    @Override
    public Material create(Material material) {
        return materialRepository.save(material);
    }

    @Override
    public Material update(Material material) {
        Optional<Material> existingMaterial =
                materialRepository.findById(material.getUuid());

        if (existingMaterial.isPresent()) {
            return existingMaterial.get();
        } else return null;
    }

    @Override
    public List<Material> findAllMaterials() {
        return materialRepository.findAllByMaterialFlagIsTrue();
    }

    @Override
    public List<Material> findAllProducts() {
        return materialRepository.findAllByProductFlagIsTrue();
    }

    @Override
    public Material findByUuid(UUID materialUuid) {
        Optional<Material> foundMaterial = materialRepository.findById(materialUuid);

        if (!foundMaterial.isPresent()) {
            return null;
        }
        return foundMaterial.get();
    }

    @Override
    public Material findMaterialByMaterialNumberCustomer(String materialNumberCustomer) {
        // TODO handle optional
        return materialRepository.findByMaterialNumberCustomerAndMaterialFlagIsTrue(materialNumberCustomer).get();
    }

    @Override
    public Material findProductByMaterialNumberCustomer(String materialNumberCustomer) {
        // TODO handle optional
        return materialRepository.findByMaterialNumberCustomerAndProductFlagIsTrue(materialNumberCustomer).get();
    }
}
