package org.eclipse.tractusx.puris.backend.stock.logic.service;

import org.eclipse.tractusx.puris.backend.stock.domain.model.PartnerProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;
import org.eclipse.tractusx.puris.backend.stock.domain.repository.PartnerProductStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PartnerProductStockServiceImpl implements PartnerProductStockService {

    @Autowired
    PartnerProductStockRepository partnerProductStockRepository;

    @Override
    public PartnerProductStock create(PartnerProductStock partnerProductStock) {
        return partnerProductStockRepository.save(partnerProductStock);
    }

    @Override
    public List<PartnerProductStock> findAll() {
        return partnerProductStockRepository.findAllByType(DT_StockTypeEnum.PRODUCT);
    }

    @Override
    public List<PartnerProductStock> findAllByMaterialUuid(UUID materialUuid) {
        return partnerProductStockRepository.findAllByMaterial_UuidAndType(materialUuid, DT_StockTypeEnum.PRODUCT);
    }

    @Override
    public PartnerProductStock update(PartnerProductStock partnerProductStock) {

        Optional<PartnerProductStock> existingStock = partnerProductStockRepository.findById(partnerProductStock.getUuid());

        if (existingStock.isPresent() && existingStock.get().getType() == DT_StockTypeEnum.PRODUCT) {
            return existingStock.get();
        } else
            return null;
    }
}
