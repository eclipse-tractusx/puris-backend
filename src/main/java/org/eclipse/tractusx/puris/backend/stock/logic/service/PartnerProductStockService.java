package org.eclipse.tractusx.puris.backend.stock.logic.service;

import org.eclipse.tractusx.puris.backend.stock.domain.model.PartnerProductStock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PartnerProductStockService {

    PartnerProductStock create(PartnerProductStock partnerProductStock);

    List<PartnerProductStock> findAll();

    List<PartnerProductStock> findAllByMaterialUuid(UUID materialUuid);

    PartnerProductStock update(PartnerProductStock partnerProductStock);
}
