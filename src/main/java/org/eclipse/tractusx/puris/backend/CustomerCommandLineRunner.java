package org.eclipse.tractusx.puris.backend;

import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Material;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.MaterialDto;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.PartnerDto;
import org.eclipse.tractusx.puris.backend.masterdata.logic.service.MaterialService;
import org.eclipse.tractusx.puris.backend.masterdata.logic.service.PartnerService;
import org.eclipse.tractusx.puris.backend.stock.domain.model.MaterialStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.MaterialStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.ProductStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.service.MaterialStockService;
import org.eclipse.tractusx.puris.backend.stock.logic.service.ProductStockService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private MaterialService materialService;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private MaterialStockService materialStockService;

    @Autowired
    private ProductStockService productStockService;

    private static final Logger log = LoggerFactory.getLogger(PurisApplication.class);

    @Override
    public void run(String... args) throws Exception {

        //supplier + material
        MaterialDto semiconductorDto = new MaterialDto(
                true,
                false,
                "MNR-7307-AU340474.002",
                "MNR-8101-ID146955.001",
                "",
                "semiconductor");
        Material semiconductorEntity = modelMapper.map(semiconductorDto,
                Material.class);
        Material createdSemiconductorEntity = materialService.create(semiconductorEntity);
        semiconductorDto = modelMapper.map(createdSemiconductorEntity, MaterialDto.class);
        log.info(String.format("Created material: %s", createdSemiconductorEntity));
        List<Material> materialsFound = materialService.findAllMaterials();
        log.info("Found Material:");
        materialsFound.stream().forEach(m -> log.info(m.toString()));

        PartnerDto supplierPartnerDto = new PartnerDto(
                "Test Supplier",
                false,
                true,
                "TODO",
                "BPNL1234567890ZZ",
                "BPNS1234567890ZZ"
        );
        List<MaterialDto> suppliesMaterial = new ArrayList<MaterialDto>();
        suppliesMaterial.add(semiconductorDto);
        supplierPartnerDto.setSuppliesMaterials(suppliesMaterial);
        Partner supplierPartnerEntity = modelMapper.map(supplierPartnerDto, Partner.class);
        supplierPartnerEntity = partnerService.create(supplierPartnerEntity);

        log.info(String.format("Created supplier partner: %s", supplierPartnerEntity));

        log.info(String.format("Relationship supplierPartner.suppliesMaterial has been persisted:" +
                        " %b",
                supplierPartnerEntity.getSuppliesMaterials()));

        // customer + material
        MaterialDto centralControlUnitDto = new MaterialDto(
                false,
                true,
                "MNR-4177-C",
                "MNR-4177-S",
                "0",
                "central control unit");

        Material centralControlUnitEntity = modelMapper.map(centralControlUnitDto,
                Material.class);
        Material createdCentralControlUnitEntity = materialService.create(centralControlUnitEntity);
        centralControlUnitDto = modelMapper.map(createdCentralControlUnitEntity, MaterialDto.class);
        log.info(String.format("Created Product: %s", createdCentralControlUnitEntity));
        List<Material> productsFound = materialService.findAllProducts();
        log.info("Found Product:");
        productsFound.stream().forEach(p -> log.info(p.toString()));

        log.info(String.format("Relationship Material.suppliedByPartners has been persisted: %b",
                productsFound.get(0).getSuppliedByPartners()));

        PartnerDto customerPartnerDto = new PartnerDto(
                "Test Customer",
                true,
                false,
                "TODO",
                "BPNL4444444444XX",
                "BPNS4444444444XX"
        );
        List<MaterialDto> ordersMaterial = new ArrayList<MaterialDto>();
        suppliesMaterial.add(centralControlUnitDto);
        customerPartnerDto.setOrdersProducts(ordersMaterial);
        Partner customerPartnerEntity = modelMapper.map(customerPartnerDto, Partner.class);
        customerPartnerEntity = partnerService.create(customerPartnerEntity);

        log.info(String.format("Created customerPartner: %s", customerPartnerEntity));
        log.info(String.format("Relationship Partner.ordersProducts has been persisted: %b",
                customerPartnerEntity.getOrdersProducts()));
        productsFound = materialService.findAllProducts();
        log.info("Found Product:");
        productsFound.stream().forEach(p -> log.info(p.toString()));
        log.info(String.format("Relationship Product.orderedByPartners has been " +
                        "persisted: %b",
                productsFound.get(0).getOrderedByPartners()));

        centralControlUnitEntity = materialService.findByUuid(centralControlUnitEntity.getUuid());
        centralControlUnitDto = modelMapper.map(centralControlUnitEntity, MaterialDto.class);

        List<PartnerDto> orderedByPartners = new ArrayList<PartnerDto>();
        orderedByPartners.add(customerPartnerDto);
        centralControlUnitDto.setOrderedByPartners(orderedByPartners);
        centralControlUnitEntity = modelMapper.map(centralControlUnitDto, Material.class);
        centralControlUnitEntity = materialService.update(centralControlUnitEntity);


        // get latest material due to relationships
        semiconductorEntity = materialService.findByUuid(semiconductorEntity.getUuid());
        semiconductorDto = modelMapper.map(createdSemiconductorEntity, MaterialDto.class);

        MaterialStockDto materialStockDto = new MaterialStockDto(
                semiconductorDto,
                20,
                "BPNS1111111110ZZ"
        );
        MaterialStock materialStockEntity = modelMapper.map(materialStockDto,
                MaterialStock.class);
        materialStockEntity = materialStockService.create(materialStockEntity);
        log.info(String.format("Created materialStock: %s", materialStockEntity));

        // get latest product due to relationships
        centralControlUnitEntity = materialService.findByUuid(centralControlUnitEntity.getUuid());
        centralControlUnitDto = modelMapper.map(centralControlUnitEntity, MaterialDto.class);

        // get latest version as Dto
        customerPartnerDto = modelMapper.map(customerPartnerDto, PartnerDto.class);

        ProductStockDto productStockDto = new ProductStockDto(
                centralControlUnitDto,
                20,
                "BPNS1111111110ZZ",
                customerPartnerDto
        );

        ProductStock productStockEntity = modelMapper.map(productStockDto,
                ProductStock.class);
        productStockEntity = productStockService.create(productStockEntity);
        log.info(String.format("Created productStock: %s", productStockEntity));

    }
}
