/*
 * This Source Code Form is subject to the terms of the "SDCcc non-commercial use license".
 *
 * Copyright (C) 2025 Draegerwerk AG & Co. KGaA
 */

package com.draeger.medical.sdccc.sdcri;

import com.draeger.medical.sdccc.configuration.TestSuiteConfig;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.Optional;
import org.somda.sdc.dpws.network.LocalAddressResolver;

/**
 * Implementation of {@linkplain LocalAddressResolver} that uses the configured address.
 */
@Singleton
public class LocalAddressResolverImpl implements LocalAddressResolver {
    private final String adapterAddress;

    @Inject
    LocalAddressResolverImpl(@Named(TestSuiteConfig.NETWORK_INTERFACE_ADDRESS) final String adapterAddress) {
        this.adapterAddress = adapterAddress;
    }

    @Override
    public Optional<String> getLocalAddress(final String remoteUri) {
        return Optional.of(adapterAddress);
    }
}
