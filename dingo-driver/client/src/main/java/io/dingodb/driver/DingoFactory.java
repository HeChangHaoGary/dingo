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

package io.dingodb.driver;

import lombok.experimental.Delegate;
import org.apache.calcite.avatica.AvaticaFactory;
import org.apache.calcite.avatica.AvaticaResultSet;
import org.apache.calcite.avatica.AvaticaStatement;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.avatica.QueryState;

import java.sql.SQLException;
import java.util.TimeZone;

public class DingoFactory implements AvaticaFactory {

    @Delegate
    private final AvaticaFactory delegate;

    public DingoFactory(AvaticaFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public AvaticaResultSet newResultSet(
        AvaticaStatement statement, QueryState state, Meta.Signature signature, TimeZone timeZone, Meta.Frame firstFrame
    ) throws SQLException {
        return new DingoResultSet(
            statement, state, signature, newResultSetMetaData(statement, signature), timeZone, firstFrame
        );
    }

}