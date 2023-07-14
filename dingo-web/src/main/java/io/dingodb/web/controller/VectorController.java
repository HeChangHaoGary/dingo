/*
 * Copyright 2021 DataCanvas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dingodb.web.controller;

import io.dingodb.client.DingoClient;
import io.dingodb.client.common.VectorDistanceArray;
import io.dingodb.client.common.VectorSearch;
import io.dingodb.web.mapper.EntityMapper;
import io.dingodb.web.model.dto.VectorGet;
import io.dingodb.web.model.dto.VectorWithId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api("Vector")
@RestController
@RequestMapping("/vector")
public class VectorController {

    @Autowired
    private DingoClient dingoClient;

    @Autowired
    private EntityMapper mapper;

    @ApiOperation("Vector add")
    @PutMapping("/api/{schema}/{index}")
    public ResponseEntity<List<VectorWithId>> vectorAdd(
        @PathVariable String schema,
        @PathVariable String index,
        @RequestBody List<VectorWithId> vectors) {
        return ResponseEntity.ok(dingoClient.vectorAdd(schema, index, vectors.stream()
            .map(mapper::mapping)
            .collect(Collectors.toList())).stream().map(mapper::mapping).collect(Collectors.toList()));
    }

    @ApiOperation("Vector delete")
    @DeleteMapping("/api/{schema}/{index}")
    public ResponseEntity<List<Boolean>> vectorDelete(
        @PathVariable String schema,
        @PathVariable String index,
        @RequestBody List<Long> ids) {
        return ResponseEntity.ok(dingoClient.vectorDelete(schema, index, ids));
    }

    @ApiOperation("Vector get")
    @PostMapping("/api/{schema}/{index}/get")
    public ResponseEntity<List<io.dingodb.client.common.VectorWithId>> vectorGet(
        @PathVariable String schema,
        @PathVariable String index,
        @RequestBody VectorGet vectorGet
        ) {
        return ResponseEntity.ok(dingoClient.vectorBatchQuery(
            schema,
            index,
            vectorGet.getIds(),
            vectorGet.getWithoutVectorData(),
            vectorGet.getWithScalarData(),
            vectorGet.getKeys()));
    }

    @ApiOperation("Get max vector id")
    @GetMapping("/api/{schema}/{index}/id")
    public ResponseEntity<Long> vectorMaxId(
        @PathVariable String schema,
        @PathVariable String index,
        Boolean isGetMin
    ) {
        return ResponseEntity.ok(dingoClient.vectorGetBorderId(schema, index, isGetMin));
    }

    @ApiOperation("Vector search")
    @PostMapping("/api/{schema}/{index}")
    public ResponseEntity<VectorDistanceArray> vectorSearch(
        @PathVariable String schema,
        @PathVariable String index,
        @RequestBody VectorSearch vectorSearch) {
        return ResponseEntity.ok(dingoClient.vectorSearch(schema, index, vectorSearch));
    }
}