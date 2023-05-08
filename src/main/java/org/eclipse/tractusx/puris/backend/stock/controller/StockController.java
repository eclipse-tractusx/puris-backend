package org.eclipse.tractusx.puris.backend.stock.controller;

import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Material;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.MaterialDto;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.PartnerDto;
import org.eclipse.tractusx.puris.backend.masterdata.logic.service.MaterialService;
import org.eclipse.tractusx.puris.backend.masterdata.logic.service.PartnerService;
import org.eclipse.tractusx.puris.backend.stock.domain.model.MaterialStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.PartnerProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.MaterialStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.PartnerProductStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.ProductStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.service.MaterialStockService;
import org.eclipse.tractusx.puris.backend.stock.logic.service.PartnerProductStockService;
import org.eclipse.tractusx.puris.backend.stock.logic.service.ProductStockService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("stockView")
public class StockController {

    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private MaterialStockService materialStockService;

    @Autowired
    private PartnerProductStockService partnerProductStockService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private PartnerService partnerService;


    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("materials")
    @ResponseBody
    public List<MaterialDto> getMaterials() {

        List<MaterialDto> allMaterials = materialService.findAllMaterials().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        allMaterials.stream().forEach(m -> System.out.println(m));

        return allMaterials;

    }

    private MaterialDto convertToDto(Material entity) {
        return modelMapper.map(entity, MaterialDto.class);
    }

    @GetMapping("products")
    @ResponseBody
    public List<MaterialDto> getProducts() {

        List<MaterialDto> allProducts = materialService.findAllProducts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return allProducts;
    }

    @GetMapping("product-stocks")
    @ResponseBody
    public List<ProductStockDto> getProductStocks() {
        List<ProductStockDto> allProductStocks = productStockService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return allProductStocks;
    }

    //TODO implement the Create or Update Product Stock call. Check for best practice of dto with relationship and how to handle Ids.
    @PostMapping("product-stocks")
    @ResponseBody
    public ProductStockDto createProductStocks(@RequestBody ProductStockDto productStockDto) {

        // TODO validate mandatory attributes here or in Frontend or both

        ProductStock productStockToCreate = convertToEntity(productStockDto);
        // TODO set last updated on to now according to ISO Timestamp

        ProductStock createdProductStock = productStockService.create(productStockToCreate);

        ProductStockDto productStockToReturn = convertToDto(createdProductStock);

        return productStockToReturn;
    }

    @PutMapping("product-stocks")
    @ResponseBody
    public ProductStockDto updateProductStocks(@RequestBody ProductStockDto productStockDto) {

        // TODO validate mandatory attributes here or in Frontend or both


        ProductStock existingProductStock = productStockService.findByUuid(productStockDto.getUuid());
        if (existingProductStock.getUuid() == null) {
            return null;
        }

        existingProductStock.setQuantity(productStockDto.getQuantity());
        // TODO set last updated on to now according to ISO Timestamp

        existingProductStock = productStockService.create(existingProductStock);

        ProductStockDto productStockToReturn = convertToDto(existingProductStock);

        return productStockToReturn;
    }

    private ProductStockDto convertToDto(ProductStock entity) {
        return modelMapper.map(entity, ProductStockDto.class);
    }

    private ProductStock convertToEntity(ProductStockDto dto) {
        return modelMapper.map(dto, ProductStock.class);
    }

    @GetMapping("material-stocks")
    @ResponseBody
    public List<MaterialStockDto> getMaterialStocks() {
        List<MaterialStockDto> allMaterialStocks = materialStockService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return allMaterialStocks;
    }

    //TODO implement the Create or Update Material Stock call. Check for best practice of dto with relationship and how to handle Ids.
    @PostMapping("material-stocks")
    @ResponseBody
    public MaterialStockDto createMaterialStocks(@RequestBody MaterialStockDto materialStockDto) {

        // TODO validate mandatory attributes here or in Frontend or both

        MaterialStock materialStockToCreate = convertToEntity(materialStockDto);
        // TODO set last updated on to now according to ISO Timestamp

        MaterialStock createdMterialStock = materialStockService.create(materialStockToCreate);

        MaterialStockDto materialStockToReturn = convertToDto(createdMterialStock);

        return materialStockToReturn;
    }

    @PutMapping("material-stocks")
    @ResponseBody
    public MaterialStockDto updateMaterialStocks(@RequestBody MaterialStockDto materialStockDto) {

        // TODO validate mandatory attributes here or in Frontend or both


        MaterialStock existingMaterialStock = materialStockService.findByUuid(materialStockDto.getUuid());
        if (existingMaterialStock.getUuid() == null) {
            return null;
        }

        existingMaterialStock.setQuantity(materialStockDto.getQuantity());
        // TODO set last updated on to now according to ISO Timestamp

        existingMaterialStock = materialStockService.create(existingMaterialStock);

        MaterialStockDto productStockToReturn = convertToDto(existingMaterialStock);

        return productStockToReturn;
    }

    private MaterialStockDto convertToDto(MaterialStock entity) {
        return modelMapper.map(entity, MaterialStockDto.class);
    }

    private MaterialStock convertToEntity(MaterialStockDto dto) {
        return modelMapper.map(dto, MaterialStock.class);
    }

    @GetMapping("partner-product-stocks")
    @ResponseBody
    public List<PartnerProductStockDto> getPartnerProductStocks() {
        List<PartnerProductStockDto> allPartnerProductStocks = partnerProductStockService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return allPartnerProductStocks;
    }

    private PartnerProductStockDto convertToDto(PartnerProductStock entity) {
        return modelMapper.map(entity, PartnerProductStockDto.class);
    }

    @GetMapping("customer")
    @ResponseBody
    public List<PartnerDto> getCustomerPartnersOrderingMaterial(UUID materialUuid) {
        List<PartnerDto> allCustomerPartners = partnerService.findAllCustomerPartnersForMaterialId(materialUuid).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return allCustomerPartners;
    }

    private PartnerDto convertToDto(Partner entity) {
        return modelMapper.map(entity, PartnerDto.class);
    }

    // TODO implement trigger of Request Api for a set of partners supplying a given material. This also creates and updates PartnerProductStock

}
