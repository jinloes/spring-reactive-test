package com.jinloes.springdoc_openapi.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Tag(name = "account")
public class AccountController {

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "List all accounts", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class))),
                    responseCode = "200")
    })
    public List<Account> accounts() {
        return Stream.of(new Account("acc1"), new Account("acc2")).collect(Collectors.toList());
    }
}
