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
import org.eclipse.tractusx.puris.backend.common.api.logic.dto.MessageHeaderDto;
import org.eclipse.tractusx.puris.backend.common.api.logic.dto.RequestDto;
import org.eclipse.tractusx.puris.backend.common.api.logic.service.RequestService;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Material;
import org.eclipse.tractusx.puris.backend.masterdata.domain.model.Partner;
import org.eclipse.tractusx.puris.backend.masterdata.logic.service.MaterialService;
import org.eclipse.tractusx.puris.backend.masterdata.logic.service.PartnerService;
import org.eclipse.tractusx.puris.backend.stock.domain.model.MaterialStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.PartnerProductStock;
import org.eclipse.tractusx.puris.backend.stock.domain.model.ProductStock;
import org.eclipse.tractusx.puris.backend.stock.logic.adapter.ProductStockSammMapper;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.ProductStockDto;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.ProductStockRequestForMaterialDto;
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
        Partner supplierPartner = new Partner(
                "Test Supplier",
                false,
                true,
                "TODO",
                "BPNL1234567890ZZ",
                "BPNS1234567890ZZ"
        );
        supplierPartner = partnerService.create(supplierPartner);
        log.info(String.format("Created SupplierPartner: %s", supplierPartner));
        supplierPartner = partnerService.findByUuid(supplierPartner.getUuid());
        log.info(String.format("Found supplier partner: %s", supplierPartner));


        Material semiconductorEntity = new Material(
                true,
                false,
                "MNR-7307-AU340474.002",
                "MNR-8101-ID146955.001",
                "",
                "semiconductor"
        );
        semiconductorEntity.addPartnerToSuppliedByPartners(supplierPartner);
        semiconductorEntity = materialService.create(semiconductorEntity);
        log.info(String.format("Created material: %s", semiconductorEntity));

        List<Material> materialsFound = materialService.findAllMaterials();
        log.info(String.format("Found Material: %s", materialsFound));

        log.info(String.format("UUID of supplier partner: %s", supplierPartner.getUuid()));
        supplierPartner = partnerService.findByUuid(supplierPartner.getUuid());
        log.info(String.format("Found supplier partner: %s", supplierPartner));
        log.info(String.format("Relationship to material: %s", supplierPartner.getSuppliesMaterials()));

        // customer + material
        Partner customerPartnerEntity = new Partner(
                "Test Customer",
                true,
                false,
                "TODO",
                "BPNL4444444444XX",
                "BPNS4444444444XX"
        );
        customerPartnerEntity = partnerService.create(customerPartnerEntity);

        Material centralControlUnitEntity = new Material(
                false,
                true,
                "MNR-4177-C",
                "MNR-4177-S",
                "0",
                "central control unit"
        );

        centralControlUnitEntity.addPartnerToOrderedByParnters(customerPartnerEntity);

        centralControlUnitEntity = materialService.create(centralControlUnitEntity);
        log.info(String.format("Created Product: %s", centralControlUnitEntity));

        List<Material> productsFound = materialService.findAllProducts();
        log.info(String.format("Found Products: %s", productsFound));

        customerPartnerEntity = partnerService.findByUuid(customerPartnerEntity.getUuid());
        log.info(String.format("Created supplier partner: %s", customerPartnerEntity));
        log.info(String.format("Relationship to product: %s",
                customerPartnerEntity.getOrdersProducts()));

        Material existingMaterial =
                materialService.findByUuid(semiconductorEntity.getUuid());
        log.info(String.format("Found existingMaterial by uuid: %s",
                existingMaterial));

        Material existingProduct =
                materialService.findProductByMaterialNumberCustomer(centralControlUnitEntity.getMaterialNumberCustomer());
        log.info(String.format("Found existingProduct by customer number: %s",
                existingMaterial));

        List<Material> existingProducts =
                materialService.findAllProducts();
        log.info(String.format("Found existingProducts by product flag true: %s",
                existingProducts));

        log.info(String.format("Relationship centralControlUnitEntity -> orderedByPartners: %s",
                centralControlUnitEntity.getOrderedByPartners().toString()));


        // Create Material Stock
        MaterialStock materialStockEntity = new MaterialStock(
                semiconductorEntity,
                20,
                "BPNS1111111110ZZ",
                new Date()
        );

        materialStockEntity = materialStockService.create(materialStockEntity);
        log.info(String.format("Created materialStock: %s", materialStockEntity));

        List<MaterialStock> foundMaterialStocks =
                materialStockService.findAllByMaterialNumberCustomer(semiconductorEntity.getMaterialNumberCustomer());
        log.info(String.format("Found materialStock: %s", foundMaterialStocks));


        // Create Product Stock
        // get latest product due to relationships

        ProductStock productStockEntity = new ProductStock(
                centralControlUnitEntity,
                20,
                "BPNS1111111110ZZ",
                new Date(),
                customerPartnerEntity
        );


        productStockEntity = productStockService.create(productStockEntity);
        log.info(String.format("Created productStock: %s", productStockEntity.toString()));

        List<ProductStock> foundProductStocks =
                productStockService
                        .findAllByMaterialNumberCustomerAndAllocatedToCustomerBpnl(
                                centralControlUnitEntity.getMaterialNumberCustomer(),
                                customerPartnerEntity.getBpnl());
        log.info(String.format("Found productStocks by material number and allocated to customer " +
                "bpnl: %s", foundProductStocks));


        // Create PartnerProductStock
        semiconductorEntity = materialService.findByUuid(semiconductorEntity.getUuid());

        supplierPartner = partnerService.findByUuid(supplierPartner.getUuid());

        PartnerProductStock partnerProductStockEntity = new PartnerProductStock(
                semiconductorEntity,
                20,
                supplierPartner.getSiteBpns(),
                new Date(),
                supplierPartner
        );

        partnerProductStockEntity = partnerProductStockService.create(partnerProductStockEntity);
        log.info(String.format("Created partnerProductStock: %s", partnerProductStockEntity));

        ProductStockDto productStockDto = modelMapper.map(productStockEntity,
                ProductStockDto.class);
        ProductStockSammDto productStockSammDto = productStockSammMapper.toSamm(productStockDto);

        log.info(objectMapper.writeValueAsString(productStockSammDto));

        // TODO check if this is smart, or if we should use entities
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

        List<ProductStockRequestForMaterialDto> messageContentDtos = new ArrayList<>();

        ProductStockRequestForMaterialDto messageContentDto = new ProductStockRequestForMaterialDto();
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
