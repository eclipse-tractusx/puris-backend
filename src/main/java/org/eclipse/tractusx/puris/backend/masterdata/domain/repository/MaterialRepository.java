package org.eclipse.tractusx.puris.backend.masterdata.domain.repository;

import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {

    List<Material> findAllByMaterialFlagIsTrue();

    List<Material> findAllByProductFlagIsTrue();

    Optional<Material> findByMaterialNumberCustomerAndMaterialFlagIsTrue(String materialNumberCustomer);

    Optional<Material> findByMaterialNumberCustomerAndProductFlagIsTrue(String materialNumberCustomer);
}
