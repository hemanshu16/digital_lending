package com.digitallending.userservice.providers.msmeuserdetails;

import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsRequestDTO;
import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import java.util.UUID;
import java.util.stream.Stream;

public class MsmeUserDetailsRequestDTOProvider implements ArgumentsProvider {
    /**
     * Provide a {@link Stream} of {@link Arguments} to be passed to a
     * {@code @ParameterizedTest} method.
     *
     * @param context the current extension context; never {@code null}
     * @return a stream of arguments; never {@code null}
     */
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f25eb"),
                MsmeUserDetailsRequestDTO
                        .builder()
                        .category(UUID.fromString("62833a96-32fd-44e9-8926-182a799dc6e6"))
                        .educationalQualification(UUID.fromString("4359b425-d47e-41b1-b794-6a2e396fd040"))
                        .gender(UUID.fromString("ec78a3d8-2fff-4867-ba91-4290fca86635"))
                        .maritalStatus(UUID.fromString("6f3bf095-3c73-4443-a9d0-694cb2b9bec1")).build())
        );
    }
}
