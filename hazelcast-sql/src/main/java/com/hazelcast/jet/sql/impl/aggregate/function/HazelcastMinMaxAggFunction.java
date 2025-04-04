/*
 * Copyright 2025 Hazelcast Inc.
 *
 * Licensed under the Hazelcast Community License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://hazelcast.com/hazelcast-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet.sql.impl.aggregate.function;

import com.hazelcast.jet.sql.impl.validate.HazelcastCallBinding;
import com.hazelcast.jet.sql.impl.validate.operators.common.HazelcastAggFunction;
import com.hazelcast.jet.sql.impl.validate.operators.typeinference.ReplaceUnknownOperandTypeInference;
import com.hazelcast.jet.sql.impl.validate.param.NoOpParameterConverter;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.util.Optionality;

import static org.apache.calcite.sql.type.SqlTypeName.BIGINT;

public class HazelcastMinMaxAggFunction extends HazelcastAggFunction {

    public HazelcastMinMaxAggFunction(SqlKind kind) {
        super(
                kind.name(),
                kind,
                ReturnTypes.ARG0_NULLABLE_IF_EMPTY,
                new ReplaceUnknownOperandTypeInference(BIGINT),
                null,
                SqlFunctionCategory.SYSTEM,
                false,
                false,
                Optionality.FORBIDDEN);
    }

    protected boolean checkOperandTypes(HazelcastCallBinding binding, boolean throwOnFailure) {
        SqlNode node = binding.operand(0);
        if (node.getKind() == SqlKind.DYNAMIC_PARAM) {
            int parameterIndex = ((SqlDynamicParam) node).getIndex();
            binding.getValidator().setParameterConverter(parameterIndex, NoOpParameterConverter.INSTANCE);
        }

        // MIN/MAX accepts any operand type (though it must be Comparable at runtime)
        return true;
    }
}
