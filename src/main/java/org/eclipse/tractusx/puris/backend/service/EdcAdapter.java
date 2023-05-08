/*
 * Copyright (c) 2022,2023 Volkswagen AG
 * Copyright (c) 2022,2023 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e.V. (represented by Fraunhofer ISST)
 * Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
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
package org.eclipse.tractusx.puris.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.okhttp.*;
import org.eclipse.tractusx.puris.backend.repository.OrderRepository;
import org.eclipse.tractusx.puris.backend.util.EDCRequestBodyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

/**
 * Service Layer of EDC Adapter. Builds and sends requests to a productEDC.
 * The EDC connection is configured using the application.properties file.
 */
@Service
public class EdcAdapter {
    private static final OkHttpClient CLIENT = new OkHttpClient();

    @Autowired
    private OrderRepository orderRepository;

    @Value("${edc.controlplane.host}")
    private String edcHost;

    @Value("${edc.controlplane.data.port}")
    private Integer dataPort;

    @Value("${edc.backend.url}")
    private String backendUrl;

    @Value("${edc.controlplane.key}")
    private String edcApiKey;

    @Value("${server.port}")
    private String serverPort;

    @Value("${minikube.ip}")
    private String minikubeIp;

    /**
     * Publish an order at own EDC.
     *
     * @param orderId id of the order to publish.
     * @return true, if order was published.
     * @throws IOException if the connection to the EDC failed.
     */
    public boolean publishOrderAtEDC(String orderId) throws IOException {
        var order = orderRepository.findByOrderId(orderId);
        if (order.isPresent()) {
            var orderUrl = "http://" + minikubeIp + ":" + serverPort + "/catena/orders/order/id/" + orderId;
            var assetBody = EDCRequestBodyBuilder.buildAssetRequestBody(orderUrl, orderId);
            var policyBody = EDCRequestBodyBuilder.buildPolicyRequestBody(orderId);
            var contractBody = EDCRequestBodyBuilder.buildContractRequestBody(orderId);
            var success = sendEdcRequest(assetBody, "/data/assets").isSuccessful();
            success &= sendEdcRequest(policyBody, "/data/policydefinitions").isSuccessful();
            success &= sendEdcRequest(contractBody, "/data/contractdefinitions").isSuccessful();
            return success;
        }
        return false;
    }

    /**
     * Get catalog from an EDC.
     *
     * @param idsUrl url of the EDC to get catalog from.
     * @return catalog of the requested EDC.
     * @throws IOException if the connection to the EDC failed.
     */
    public String getCatalog(String idsUrl) throws IOException {
        var request = new Request.Builder()
                .get()
                .url(new HttpUrl.Builder()
                        .scheme("http")
                        .host(edcHost)
                        .port(dataPort)
                        .addPathSegment("data")
                        .addPathSegment("catalog")
                        .addEncodedQueryParameter("providerUrl", idsUrl)
                        .build()
                )
                .header("X-Api-Key", edcApiKey)
                .header("Content-Type", "application/json")
                .build();
        var response = CLIENT.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.body().string());
        }
        return response.body().string();
    }

    /**
     * Get received data from a transfer from edc backend application.
     *
     * @param transferId id of the transfer.
     * @return received data from transfer.
     * @throws IOException if the connection to the backend application failed.
     */
    public String getFromBackend(String transferId) throws IOException {
        var request = new Request.Builder()
                .get()
                .url(new URL(String.format("%s/%s", backendUrl, transferId)))
                .header("Accept", "application/octet-stream")
                .build();
        var response = CLIENT.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Start a negotitation with another EDC.
     *
     * @param connectorAddress ids url of the negotiation counterparty.
     * @param orderId id of the negotiations target asset.
     * @return response body received from the EDC.
     * @throws IOException if the connection to the EDC failed.
     */
    public String startNegotiation(String connectorAddress, String orderId) throws IOException {
        var negotiationRequestBody = EDCRequestBodyBuilder.buildNegotiationRequestBody(connectorAddress, orderId);
        var response = sendEdcRequest(negotiationRequestBody, "/data/contractnegotiations");
        return response.body().string();
    }

    /**
     * Start a data transfer with another EDC.
     *
     * @param transferId id created for the transferprocess.
     * @param connectorAddress ids url of the transfer counterparty.
     * @param contractId id of the negotiated contract.
     * @param orderId id of the transfers target asset.
     * @return response body received from the EDC.
     * @throws IOException if the connection to the EDC failed.
     */
    public String startTransfer(String transferId,
                                String connectorAddress,
                                String contractId,
                                String orderId) throws IOException {
        var transferNode = EDCRequestBodyBuilder.buildTransferRequestBody(transferId, connectorAddress, contractId, orderId);
        var response = sendEdcRequest(transferNode, "/data/transferprocess");
        return response.body().string();
    }

    /**
     * Delete an asset from the own EDC.
     *
     * @param assetId id of the asset to delete.
     * @return response body received from the EDC.
     * @throws IOException if the connection to the EDC failed.
     */
    public String deleteAsset(String assetId) throws IOException {
        var urlBuilder = new HttpUrl.Builder()
                .scheme("http")
                .host(edcHost)
                .port(dataPort);
        urlBuilder.addPathSegment("data");
        urlBuilder.addPathSegment("assets");
        urlBuilder.addPathSegment(assetId);
        var url = urlBuilder.build();
        var request = new Request.Builder()
                .url(url)
                .header("X-Api-Key", edcApiKey)
                .header("Content-Type", "application/json")
                .delete()
                .build();
        var response = CLIENT.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Send a GET request to the own EDC.
     *
     * @param resourceId (optional) id of the resource to request, will be left empty if null.
     * @param pathSegments varargs for the path segments of the request
     *                     (e.g "data", "assets" will be turned to /data/assets).
     * @return response body received from the EDC.
     * @throws IOException if the connection to the EDC failed.
     */
    public String getFromEdc(String resourceId, String... pathSegments) throws IOException {
        var urlBuilder = new HttpUrl.Builder()
                .scheme("http")
                .host(edcHost)
                .port(dataPort);
        for(var seg : pathSegments) {
            urlBuilder.addPathSegment(seg);
        }
        if(resourceId != null) {
            urlBuilder.addPathSegment(resourceId);
        }
        var url = urlBuilder.build();
        var request = new Request.Builder()
                .get()
                .url(url)
                .header("X-Api-Key", edcApiKey)
                .header("Content-Type", "application/json")
                .build();
        var response = CLIENT.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Util method for building a http POST request to the own EDC.
     *
     * @param requestBody requestBody to be sent to the EDC.
     * @param urlSuffix path to POST data to
     * @return response received from the EDC.
     * @throws IOException if the connection to the EDC failed.
     */
    private Response sendEdcRequest(JsonNode requestBody, String urlSuffix) throws IOException {
        var request = new Request.Builder()
                .header("X-Api-Key", edcApiKey)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                .url("http://" + edcHost + ":" + dataPort + urlSuffix)
                .build();
        return CLIENT.newCall(request).execute();
    }

}