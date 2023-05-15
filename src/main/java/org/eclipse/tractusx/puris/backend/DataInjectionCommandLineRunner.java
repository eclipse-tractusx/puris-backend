/*
 * Copyright (c) 2023 Volkswagen AG
 * Copyright (c) 2023 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e.V.
 * (represented by Fraunhofer ISST)
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.eclipse.tractusx.puris.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.puris.backend.common.api.domain.model.Request;
import org.eclipse.tractusx.puris.backend.common.api.domain.model.datatype.DT_RequestStateEnum;
import org.eclipse.tractusx.puris.backend.common.api.domain.model.datatype.DT_UseCaseEnum;
import org.eclipse.tractusx.puris.backend.common.api.logic.dto.MessageContentDto;
import org.eclipse.tractusx.puris.backend.common.api.logic.dto.MessageContentErrorDto;
import org.eclipse.tractusx.puris.backend.common.api.logic.dto.MessageHeaderDto;
import org.eclipse.tractusx.puris.backend.common.api.logic.dto.RequestDto;
import org.eclipse.tractusx.puris.backend.common.api.logic.service.RequestService;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Material;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.MaterialDto;
import org.eclipse.tractusx.puris.backend.masterdata.logic.dto.PartnerDto;
import org.eclipse.tractusx.puris.backend.masterdata.logic.service.MaterialService;
import org.eclipse.tractusx.puris.backend.masterdata.logic.service.PartnerService;
import org.eclipse.tractusx.puris.backend.stock.domain.model.MaterialStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.PartnerProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.Stock;
import org.eclipse.tractusx.puris.backend.stock.logic.adapter.ProductStockSammMapper;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.MaterialStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.PartnerProductStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.ProductStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.samm.ProductStockSammDto;
import org.eclipse.tractusx.puris.backend.stock.logic.service.MaterialStockService;
import org.eclipse.tractusx.puris.backend.stock.logic.service.PartnerProductStockService;
import org.eclipse.tractusx.puris.backend.stock.logic.service.ProductStockService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class DataInjectionCommandLineRunner implements CommandLineRunner {

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

    @Autowired
    private PartnerProductStockService partnerProductStockService;

    @Autowired
    private ProductStockSammMapper productStockSammMapper;

    @Autowired
    private RequestService requestService;

    private ObjectMapper objectMapper;

    public DataInjectionCommandLineRunner(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
        supplierPartnerDto.addSuppliedMaterial(semiconductorDto);
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
        customerPartnerDto.addOrderedProduct(centralControlUnitDto);
        Partner customerPartnerEntity = modelMapper.map(customerPartnerDto, Partner.class);
        customerPartnerEntity = partnerService.create(customerPartnerEntity);
        customerPartnerDto = modelMapper.map(customerPartnerEntity, PartnerDto.class);

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

        centralControlUnitDto.addOrderedByPartner(customerPartnerDto);
        centralControlUnitEntity = modelMapper.map(centralControlUnitDto, Material.class);
        centralControlUnitEntity = materialService.update(centralControlUnitEntity);

        //also update partner
        customerPartnerEntity = modelMapper.map(customerPartnerDto, Partner.class);
        customerPartnerEntity = partnerService.create(customerPartnerEntity);
        customerPartnerDto = modelMapper.map(customerPartnerEntity, PartnerDto.class);


        // Create Material Stock
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


        // Create Product Stock
        // get latest product due to relationships
        centralControlUnitEntity = materialService.findByUuid(centralControlUnitEntity.getUuid());
        centralControlUnitDto = modelMapper.map(centralControlUnitEntity, MaterialDto.class);

        customerPartnerEntity = partnerService.findByUuid(customerPartnerEntity.getUuid());
        customerPartnerDto = modelMapper.map(customerPartnerEntity, PartnerDto.class);

        ProductStockDto productStockDto = new ProductStockDto(
                centralControlUnitDto,
                20,
                "BPNS1111111110ZZ",
                customerPartnerDto
        );

        customerPartnerEntity = modelMapper.map(customerPartnerDto, Partner.class);
        customerPartnerEntity = partnerService.update(customerPartnerEntity);

        ProductStock productStockEntity = modelMapper.map(productStockDto,
                ProductStock.class);
        productStockEntity = productStockService.create(productStockEntity);
        log.info(String.format("Created productStock: %s", ((Stock) productStockEntity).toString()));


        // Create PartnerProductStock
        semiconductorEntity = materialService.findByUuid(semiconductorEntity.getUuid());
        semiconductorDto = modelMapper.map(createdSemiconductorEntity, MaterialDto.class);

        supplierPartnerEntity = partnerService.findByUuid(supplierPartnerEntity.getUuid());
        supplierPartnerDto = modelMapper.map(supplierPartnerEntity, PartnerDto.class);

        PartnerProductStockDto partnerProductStockDto = new PartnerProductStockDto(
                semiconductorDto,
                20,
                supplierPartnerDto.getSiteBpns(),
                supplierPartnerDto
        );
        PartnerProductStock partnerProductStockEntity = modelMapper.map(partnerProductStockDto,
                PartnerProductStock.class);
        partnerProductStockEntity = partnerProductStockService.create(partnerProductStockEntity);
        log.info(String.format("Created partnerProductStock: %s", partnerProductStockEntity));

        productStockDto = modelMapper.map(productStockEntity, ProductStockDto.class);
        ProductStockSammDto productStockSammDto = productStockSammMapper.toSamm(productStockDto);

        log.info(objectMapper.writeValueAsString(productStockSammDto));

        MessageHeaderDto messageHeaderDto = new MessageHeaderDto();
        messageHeaderDto.setRequestId(UUID.fromString("4979893e-dd6b-43db-b732-6e48b4ba35b3"));
        messageHeaderDto.setRespondAssetId("product-stock-response-api");
        messageHeaderDto.setContractAgreementId("some cid");
        messageHeaderDto.setSender("BPNL1234567890ZZ");
        messageHeaderDto.setSenderEdc("http://sender-edc.de");
        messageHeaderDto.setReceiver("http://your-edc.de");
        messageHeaderDto.setUseCase(DT_UseCaseEnum.PURIS);
        messageHeaderDto.setCreationDate(new Date());

        log.info(objectMapper.writeValueAsString(messageHeaderDto));

        List<MessageContentDto> messageContentDtos = new ArrayList<>();

        MessageContentDto messageContentDto = new MessageContentErrorDto();
        messageContentDtos.add(messageContentDto);

        RequestDto requestDto = new RequestDto(
                DT_RequestStateEnum.RECEIPT,
                messageHeaderDto,
                messageContentDtos
        );

        Request createdRequest = requestService.createRequest(modelMapper.map(requestDto,
                Request.class));
        log.info(String.format("Created Request: %s", createdRequest));

    }
}
