package org.eclipse.tractusx.puris.backend.masterdata.logic.service;

import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PartnerService {

    Partner create(Partner partner);

    List<Partner> findAllCustomerPartnersForMaterialId(UUID materialUuid);

    Partner update(Partner partner);

}
