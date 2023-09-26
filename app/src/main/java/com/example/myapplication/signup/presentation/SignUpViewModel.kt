package com.example.myapplication.signup.presentation

import androidx.lifecycle.ViewModel
import com.example.myapplication.signup.presentation.status.NameValidationStatus
import com.example.myapplication.signup.presentation.status.PasswordValidationStatus
import com.example.myapplication.signup.presentation.status.RePasswordValidationStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class SignUpViewModel() : ViewModel() {

    private val _stateFlow = MutableStateFlow<Boolean>(false)
    val stateFlow: Flow<Boolean> = _stateFlow

    private val _firstNameValidationFlow =
        MutableStateFlow<NameValidationStatus>(NameValidationStatus.NOT_VALIDATED)
    val firstNameValidationFlow: Flow<NameValidationStatus> = _firstNameValidationFlow

    private val _lastNameValidationFlow =
        MutableStateFlow<NameValidationStatus>(NameValidationStatus.NOT_VALIDATED)
    val lastNameValidationFlow: Flow<NameValidationStatus> = _lastNameValidationFlow

    private val _rePasswordValidationFlow =
        MutableStateFlow<RePasswordValidationStatus>(RePasswordValidationStatus.NOT_VALIDATED)
    val rePasswordValidationFlow: Flow<RePasswordValidationStatus> = _rePasswordValidationFlow

    private val _passwordValidationFlow =
        MutableStateFlow<PasswordValidationStatus>(PasswordValidationStatus.NOT_VALIDATED)
    val passwordValidationFlow: Flow<PasswordValidationStatus> = _passwordValidationFlow


    fun validateFirstNameText(text: String) {
        val regex = "^[A-ZА-Я][a-zа-я]{1,20}".toRegex()
        if (!regex.matches(text)) {
            when {
                text.isEmpty() -> _firstNameValidationFlow.value = NameValidationStatus.EMPTY
                !text[0].isUpperCase() -> _firstNameValidationFlow.value =
                    NameValidationStatus.FIRST_LETTER_LOWER_CASE

                text.length < 2 -> _firstNameValidationFlow.value = NameValidationStatus.TO_SHORT
                text.length > 21 -> _firstNameValidationFlow.value = NameValidationStatus.TO_LONG
                else -> _firstNameValidationFlow.value = NameValidationStatus.CONTAINS_NUMBER
            }
        } else {
            _firstNameValidationFlow.value = NameValidationStatus.VALID
        }
        validateRegistration()
    }

    fun validateLastNameText(text: String) {
        val regex = "^[A-ZА-Я][a-zа-я]{1,20}".toRegex()
        if (!regex.matches(text)) {
            when {
                text.isEmpty() -> _lastNameValidationFlow.value = NameValidationStatus.EMPTY
                !text[0].isUpperCase() -> _lastNameValidationFlow.value =
                    NameValidationStatus.FIRST_LETTER_LOWER_CASE

                text.length < 2 -> _lastNameValidationFlow.value = NameValidationStatus.TO_SHORT
                text.length > 21 -> _lastNameValidationFlow.value = NameValidationStatus.TO_LONG
                else -> _lastNameValidationFlow.value = NameValidationStatus.CONTAINS_NUMBER
            }
        } else {
            _lastNameValidationFlow.value = NameValidationStatus.VALID
        }
        validateRegistration()
    }

    fun validatePassword(text: String) {
        val regex = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)[a-zA-Zа-яА-Я\\d]{8,}".toRegex()
        if (!regex.matches(text)) {
            when {
                text.isEmpty() -> _passwordValidationFlow.value = PasswordValidationStatus.EMPTY
                text.length < 8 -> _passwordValidationFlow.value = PasswordValidationStatus.TO_SHORT
                !text.contains("[A-ZА-Я]".toRegex()) -> _passwordValidationFlow.value =
                    PasswordValidationStatus.NOT_CONTAINS_UPPER_CASE

                !text.contains("[a-zа-я]".toRegex()) -> _passwordValidationFlow.value =
                    PasswordValidationStatus.NOT_CONTAINS_LOWER_CASE

                !text.contains("0-9".toRegex()) -> _passwordValidationFlow.value =
                    PasswordValidationStatus.NOT_CONTAINS_NUMBER
            }
        } else {
            _passwordValidationFlow.value = PasswordValidationStatus.VALID
        }
        validateRegistration()
    }

    fun validateRePassword(text: String, retext: String) {
        if (text == retext) _rePasswordValidationFlow.value = RePasswordValidationStatus.VALID
        else _rePasswordValidationFlow.value = RePasswordValidationStatus.PASSWORD_NOT_EQUAL
        validateRegistration()
    }

    private fun validateRegistration() {
        _stateFlow.value =
            (_firstNameValidationFlow.value == NameValidationStatus.VALID) and (_lastNameValidationFlow.value == NameValidationStatus.VALID) and (_passwordValidationFlow.value == PasswordValidationStatus.VALID) and (_rePasswordValidationFlow.value == RePasswordValidationStatus.VALID)
    }
}