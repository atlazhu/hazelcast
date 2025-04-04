/*
 * Copyright (c) 2008-2025, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.internal.management.operation;

import com.hazelcast.instance.impl.DefaultNodeExtension;
import com.hazelcast.internal.config.LicenseKey;
import com.hazelcast.internal.dynamicconfig.ClusterWideConfigurationService;
import com.hazelcast.internal.dynamicconfig.ConfigurationService;
import com.hazelcast.internal.management.ManagementDataSerializerHook;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

/**
 * Operation to update license at runtime.
 */
public class SetLicenseOperation extends AbstractManagementOperation {

    private String licenseKey;

    public SetLicenseOperation() {
    }

    public SetLicenseOperation(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    @Override
    public void run() throws Exception {
        DefaultNodeExtension nodeExtension
                = (DefaultNodeExtension) getNodeEngine().getNode().getNodeExtension();
        nodeExtension.setLicenseKey(licenseKey);

        LicenseKey licenseKeyObject = new LicenseKey(licenseKey);
        ConfigurationService configurationService
                = getNodeEngine().getService(ClusterWideConfigurationService.SERVICE_NAME);
        configurationService.persist(licenseKeyObject);
    }

    @Override
    public int getClassId() {
        return ManagementDataSerializerHook.SET_LICENSE;
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        licenseKey = in.readString();
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeString(licenseKey);
    }

}
