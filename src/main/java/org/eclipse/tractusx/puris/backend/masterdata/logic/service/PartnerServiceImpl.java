package org.eclipse.tractusx.puris.backend.masterdata.logic.service;

import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;
import org.eclipse.tractusx.puris.backend.masterdata.domain.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;


    @Override
    public Partner create(Partner partner) {
        return partnerRepository.save(partner);
    }

    @Override
    public List<Partner> findAllCustomerPartnersForMaterialId(UUID materialUuid) {
        return partnerRepository.findAllByActsAsCustomerFlagIsTrueAndOrdersProducts_Uuid(materialUuid);
    }

    @Override
    public Partner update(Partner partner) {
        Optional<Partner> existingPartner =
                partnerRepository.findById(partner.getUuid());

        if (existingPartner.isPresent()) {
            return existingPartner.get();
        } else return null;
    }
}
