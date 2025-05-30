/*
 * This Source Code Form is subject to the terms of the "SDCcc non-commercial use license".
 *
 * Copyright (C) 2025 Draegerwerk AG & Co. KGaA
 */

package com.draeger.medical.sdccc.tests.biceps.direct;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.draeger.medical.sdccc.configuration.EnabledTestConfig;
import com.draeger.medical.sdccc.manipulation.Manipulations;
import com.draeger.medical.sdccc.sdcri.testclient.TestClient;
import com.draeger.medical.sdccc.tests.InjectorTestBase;
import com.draeger.medical.sdccc.tests.annotations.TestDescription;
import com.draeger.medical.sdccc.tests.annotations.TestIdentifier;
import com.draeger.medical.sdccc.tests.util.ImpliedValueUtil;
import com.draeger.medical.sdccc.tests.util.NoTestData;
import com.draeger.medical.sdccc.util.MessageGeneratingUtil;
import com.draeger.medical.sdccc.util.MessagingException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.somda.sdc.biceps.model.message.GetMdibResponse;
import org.somda.sdc.biceps.model.participant.AbstractContextDescriptor;
import org.somda.sdc.biceps.model.participant.AbstractContextState;
import org.somda.sdc.biceps.model.participant.ContextAssociation;
import org.somda.sdc.biceps.model.participant.MdsDescriptor;

/**
 * BICEPS participant model context state tests (ch. 5.4.4).
 */
public class DirectParticipantModelContextStateTest extends InjectorTestBase {

    private MessageGeneratingUtil messageGeneratingUtil;
    private Manipulations manipulations;
    private TestClient testClient;

    @BeforeEach
    void setUp() {
        testClient = getInjector().getInstance(TestClient.class);
        assertTrue(testClient.isClientRunning());
        this.messageGeneratingUtil = getInjector().getInstance(MessageGeneratingUtil.class);
        this.manipulations = getInjector().getInstance(Manipulations.class);
    }

    @Test
    @TestIdentifier(EnabledTestConfig.BICEPS_R0125)
    @TestDescription("Tries to associate a new context state for each context descriptor and verifies, that"
            + " the state is present and AbstractContextState/@ContextAssociation is set to \"Assoc\".")
    void testRequirement0125() throws NoTestData, MessagingException {
        final var getMdibResponse = messageGeneratingUtil.getMdib();
        final var mdib = (GetMdibResponse)
                getMdibResponse.getOriginalEnvelope().getBody().getAny().get(0);
        final var mdDescription = mdib.getMdib().getMdDescription();
        assertTestData(mdDescription != null, "Mdib without MdDescription present, no data");
        assertNotNull(mdDescription); // make spotbugs happy, should never happen
        final var systemContexts = mdDescription.getMds().stream()
                .map(MdsDescriptor::getSystemContext)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        var contextSeen = 0;
        for (var systemContext : systemContexts) {
            contextSeen += associateContextState(systemContext.getPatientContext());
            contextSeen += associateContextState(systemContext.getLocationContext());
            contextSeen += associateContextStates(systemContext.getEnsembleContext());
            contextSeen += associateContextStates(systemContext.getMeansContext());
            contextSeen += associateContextStates(systemContext.getOperatorContext());
            contextSeen += associateContextStates(systemContext.getWorkflowContext());
        }
        assertTestData(contextSeen, "No context supported to perform test on, test failed.");
    }

    private int associateContextStates(final List<? extends AbstractContextDescriptor> contextDescriptors) {
        for (var descriptor : contextDescriptors) {
            associateContextState(descriptor);
        }
        return contextDescriptors.size();
    }

    private int associateContextState(@Nullable final AbstractContextDescriptor contextDescriptor) {
        if (contextDescriptor == null) {
            return 0;
        }
        final var newHandle = manipulations
                .createContextStateWithAssociation(contextDescriptor.getHandle(), ContextAssociation.ASSOC)
                .getResponse();

        assertNotNull(
                newHandle, String.format("Manipulation was unsuccessful for handle %s", contextDescriptor.getHandle()));

        final var newContextState =
                testClient.getSdcRemoteDevice().getMdibAccess().getState(newHandle, AbstractContextState.class);

        assertFalse(newContextState.isEmpty(), String.format("State with handle %s is not present", newHandle));
        assertSame(
                ContextAssociation.ASSOC,
                ImpliedValueUtil.getContextAssociation(newContextState.orElseThrow()),
                String.format(
                        "Association of state with handle %s is %s instead of Assoc",
                        newHandle, ImpliedValueUtil.getContextAssociation(newContextState.orElseThrow())));
        return 1;
    }
}
