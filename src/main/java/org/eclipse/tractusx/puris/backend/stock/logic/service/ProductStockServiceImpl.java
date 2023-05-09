package org.eclipse.tractusx.puris.backend.stock.logic.service;

import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.datatype.DT_StockTypeEnum;
import org.eclipse.tractusx.puris.backend.stock.domain.repository.ProductStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductStockServiceImpl implements ProductStockService {

    @Autowired
    ProductStockRepository productStockRepository;

    @Override
    public ProductStock create(ProductStock productStock) {
        return productStockRepository.save(productStock);
    }

    @Override
    public List<ProductStock> findAll() {
        return productStockRepository.findAllByType(DT_StockTypeEnum.PRODUCT);
    }

    @Override
    public ProductStock findByUuid(UUID productStockUuid) {
        Optional<ProductStock> foundProductStock =
                productStockRepository.findById(productStockUuid);

        if (!foundProductStock.isPresent()) {
            return null;
        }
        return foundProductStock.get();
    }

    @Override
    public List<ProductStock> findAllByMaterialNumberCustomer(String materialNumberCustomer) {
        return productStockRepository.findAllByMaterial_MaterialNumberCustomerAndType(materialNumberCustomer, DT_StockTypeEnum.MATERIAL);
    }

    @Override
    public ProductStock update(ProductStock productStock) {

        Optional<ProductStock> existingStock = productStockRepository.findById(productStock.getUuid());

        if (existingStock.isPresent() && existingStock.get().getType() == DT_StockTypeEnum.PRODUCT) {
            return existingStock.get();
        } else
            return null;
    }
}
