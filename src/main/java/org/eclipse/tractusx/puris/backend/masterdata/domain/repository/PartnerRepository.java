package org.eclipse.tractusx.puris.backend.masterdata.domain.repository;

import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PartnerRepository extends JpaRepository<Partner, UUID> {

    List<Partner> findAllByActsAsCustomerFlagIsTrue();

    List<Partner> findAllByActsAsCustomerFlagIsTrueAndOrdersProducts_Uuid(UUID materialUuid);

    List<Partner> findAllByActsAsSupplierFlagIsTrue();

}
