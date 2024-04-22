package com.digitallending.userservice.providers.msmeuserdetails;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;
import java.util.stream.Stream;

public class MsmeUserDocumentProvider implements ArgumentsProvider {
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
                        UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f2001"),
                        new MockMultipartFile("Martial Status", new byte[]{3})),
                Arguments.of(UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f25eb"),
                        UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f2002"),
                        new MockMultipartFile("Category Document", new byte[]{4})),
                Arguments.of(UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f25eb"),
                        UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f2003"),
                        new MockMultipartFile("Education Qualification", new byte[]{5}))

        );
    }
}
