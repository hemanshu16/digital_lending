package com.digitallending.userservice.providers;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.model.dto.userdetails.BankAccountDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.UUID;
import java.util.stream.Stream;

public class BankAccountProvider implements ArgumentsProvider {
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
                Arguments.of(UUID.randomUUID(),
                        BankAccountDTO.builder()
                                .bankAccountId(UUID.randomUUID())
                                .accountHolderName("Hemanshu Faldu")
                                .bankName("SBI")
                                .accountNumber("123456789123")
                                .ifscCode("ICIC0000269")
                                .build(), Role.MSME),
                Arguments.of(UUID.randomUUID(),
                        BankAccountDTO.builder()
                                .bankAccountId(UUID.randomUUID())
                                .accountHolderName("Viren")
                                .bankName("SBI")
                                .accountNumber("123456789123")
                                .ifscCode("ICIC0000269")
                                .build(), Role.LENDER));
    }
}
