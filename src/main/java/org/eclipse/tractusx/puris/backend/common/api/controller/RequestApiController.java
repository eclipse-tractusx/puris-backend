/*
 * Copyright (c) 2023 Volkswagen AG
 * Copyright (c) 2023 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e.V. (represented by Fraunhofer ISST)
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
package org.eclipse.tractusx.puris.backend.common.api.controller;

import org.eclipse.tractusx.puris.backend.common.api.controller.exception.RequestIdAlreadyUsedException;
import org.eclipse.tractusx.puris.backend.common.api.domain.model.Request;
import org.eclipse.tractusx.puris.backend.common.api.domain.model.datatype.DT_RequestStateEnum;
import org.eclipse.tractusx.puris.backend.common.api.logic.dto.RequestDto;
import org.eclipse.tractusx.puris.backend.common.api.logic.dto.SuccessfullRequestDto;
import org.eclipse.tractusx.puris.backend.common.api.logic.service.RequestApiService;
import org.eclipse.tractusx.puris.backend.common.api.logic.service.RequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * The controller implements the overall flow of receiving any RequestApi call.
 * <p>
 * Subclasses should implement the specifics, such as routes.
 */
@Controller
public abstract class RequestApiController {

    /**
     *
     */
    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestApiService requestApiService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Receive a request from a consuming partner
     * <p>
     * Uses the {@link RequestApiService#handleRequest(Request)} method to perform the actual
     * task asynchronously.
     *
     * @param requestDto request to be mapped
     */
    @PostMapping
    protected ResponseEntity postRequest(@RequestBody RequestDto requestDto) {

        UUID requestId = requestDto.getHeader().getRequestId();

        Request requestFound =
                requestService.findRequestByHeaderUuid(requestId);

        if (requestFound != null) {
            throw new RequestIdAlreadyUsedException(requestId);
        }

        Request requestEntity = modelMapper.map(requestDto, Request.class);
        requestEntity.setState(DT_RequestStateEnum.RECEIPT);
        requestEntity = requestService.createRequest(requestEntity);

        // handling the request and responding should be done asynchronously.
        final Request threadRequest = requestEntity;
        Thread respondAsyncThread = new Thread(() -> {
            requestApiService.handleRequest(threadRequest);
        });
        respondAsyncThread.start();

        // if the request has been correctly taken over, return 201
        return new ResponseEntity(new SuccessfullRequestDto(requestId), HttpStatus.CREATED);
    }
}
