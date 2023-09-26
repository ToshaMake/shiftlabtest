package com.example.myapplication.signup.presentation.status

enum class PasswordValidationStatus {
    NOT_VALIDATED,
    VALID,
    NOT_CONTAINS_UPPER_CASE,
    NOT_CONTAINS_LOWER_CASE,
    NOT_CONTAINS_NUMBER,
    TO_SHORT,
    EMPTY
}