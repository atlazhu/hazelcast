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

package com.hazelcast.jet.sql.impl.opt.physical;

import com.hazelcast.function.FunctionEx;
import com.hazelcast.jet.aggregate.AggregateOperation;
import com.hazelcast.jet.sql.impl.ObjectArrayKey;
import com.hazelcast.jet.sql.impl.opt.OptUtils;
import com.hazelcast.sql.impl.QueryParameterMetadata;
import com.hazelcast.sql.impl.plan.node.PlanNodeSchema;
import com.hazelcast.sql.impl.row.JetSqlRow;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.util.ImmutableBitSet;

import java.util.ArrayList;
import java.util.List;

public class AggregateByKeyPhysicalRel extends Aggregate implements PhysicalRel {

    private final AggregateOperation<?, JetSqlRow> aggrOp;

    AggregateByKeyPhysicalRel(
            RelOptCluster cluster,
            RelTraitSet traits,
            RelNode input,
            ImmutableBitSet groupSet,
            List<ImmutableBitSet> groupSets,
            List<AggregateCall> aggCalls,
            AggregateOperation<?, JetSqlRow> aggrOp
    ) {
        super(cluster, traits, new ArrayList<>(), input, groupSet, groupSets, aggCalls);

        this.aggrOp = aggrOp;
    }

    public FunctionEx<JetSqlRow, ObjectArrayKey> groupKeyFn() {
        return ObjectArrayKey.projectFn(getGroupSet().toArray());
    }

    public AggregateOperation<?, JetSqlRow> aggrOp() {
        return aggrOp;
    }

    @Override
    public PlanNodeSchema schema(QueryParameterMetadata parameterMetadata) {
        return OptUtils.schema(getRowType());
    }

    @Override
    public <V> V accept(CreateDagVisitor<V> visitor) {
        return visitor.onAggregateByKey(this);
    }

    @Override
    public final Aggregate copy(
            RelTraitSet traitSet,
            RelNode input,
            ImmutableBitSet groupSet,
            List<ImmutableBitSet> groupSets,
            List<AggregateCall> aggCalls
    ) {
        return new AggregateByKeyPhysicalRel(getCluster(), traitSet, input, groupSet, groupSets, aggCalls, aggrOp);
    }
}
