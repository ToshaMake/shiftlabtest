package com.example.myapplication.signup.presentation.status

enum class NameValidationStatus {
    NOT_VALIDATED,
    VALID,
    FIRST_LETTER_LOWER_CASE,
    CONTAINS_NUMBER,
    TO_SHORT,
    TO_LONG,
    EMPTY
}