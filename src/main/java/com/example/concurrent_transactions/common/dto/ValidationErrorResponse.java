package com.example.concurrent_transactions.common.dto;

import java.util.List;

public record ValidationErrorResponse(List<FieldError> errors) {
}
