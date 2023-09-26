package com.example.myapplication.signup.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.myapplication.R

import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.myapplication.signup.presentation.SignUpViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import com.example.myapplication.signup.presentation.status.NameValidationStatus
import com.example.myapplication.signup.presentation.status.PasswordValidationStatus
import com.example.myapplication.signup.presentation.status.RePasswordValidationStatus
import com.example.myapplication.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private companion object {
        const val FIRST_NAME_KEY = "firstName"
        const val REQUEST_KEY = "requestKey"
        const val PREFERENCE_NAME = "pref"
    }

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel = SignUpViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registrationButton.isEnabled = false
        binding.datePicker.maxDate = Calendar.getInstance().timeInMillis
        isUserRegistered()
        initOnClickListener()
        subscribeFlow()
        initOnFocusChangeListener()
    }

    private fun isUserRegistered() {
        val sharedPreference = requireContext().getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        if (sharedPreference.getString(FIRST_NAME_KEY, null) != null) {
            setFragmentResult(
                REQUEST_KEY,
                bundleOf(FIRST_NAME_KEY to sharedPreference.getString(FIRST_NAME_KEY, null))
            )
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_firstFragment_to_secondFragment)
        }
    }


    private fun initOnClickListener() {
        binding.registrationButton.setOnClickListener {
            val editor = requireContext().getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit()
            editor.apply() {
                putString(REQUEST_KEY, binding.textInputFirstname.text.toString())
                apply()
            }
            setFragmentResult(
                REQUEST_KEY, bundleOf(FIRST_NAME_KEY to binding.textInputFirstname.text.toString())
            )
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_firstFragment_to_secondFragment)
        }
    }

    private fun subscribeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateFlow.onEach {
                binding.registrationButton.isEnabled = it
            }.collect()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.firstNameValidationFlow.onEach {
                binding.textInputFirstname.error = getFirstNameErrorStatus(it)
            }.collect()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.lastNameValidationFlow.onEach {
                binding.textInputLastname.error = getLastNameErrorStatus(it)
            }.collect()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rePasswordValidationFlow.onEach {
                binding.textInputRepassword.error = getRePasswordErrorStatus(it)
            }.collect()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.passwordValidationFlow.onEach {
                binding.textInputPassword.error = getPasswordErrorStatus(it)
            }.collect()
        }
    }

    private fun getFirstNameErrorStatus(errorStatus: NameValidationStatus): String? =
        when (errorStatus) {
            NameValidationStatus.NOT_VALIDATED -> null
            NameValidationStatus.VALID -> null
            NameValidationStatus.FIRST_LETTER_LOWER_CASE -> getString(R.string.error_first_letter_lower_case)
            NameValidationStatus.CONTAINS_NUMBER -> getString(R.string.error_contains_number)
            NameValidationStatus.TO_SHORT -> getString(R.string.error_to_short)
            NameValidationStatus.TO_LONG -> getString(R.string.error_to_long)
            NameValidationStatus.EMPTY -> getString(R.string.error_empty)
        }

    private fun getLastNameErrorStatus(errorStatus: NameValidationStatus): String? =
        when (errorStatus) {
            NameValidationStatus.NOT_VALIDATED -> null
            NameValidationStatus.VALID -> null
            NameValidationStatus.FIRST_LETTER_LOWER_CASE -> getString(R.string.error_first_letter_lower_case)
            NameValidationStatus.CONTAINS_NUMBER -> getString(R.string.error_contains_number)
            NameValidationStatus.TO_SHORT -> getString(R.string.error_to_short)
            NameValidationStatus.TO_LONG -> getString(R.string.error_to_long)
            NameValidationStatus.EMPTY -> getString(R.string.error_empty)
        }

    private fun getRePasswordErrorStatus(errorStatus: RePasswordValidationStatus): String? =
        when (errorStatus) {
            RePasswordValidationStatus.NOT_VALIDATED -> null
            RePasswordValidationStatus.VALID -> null
            RePasswordValidationStatus.PASSWORD_NOT_EQUAL -> getString(R.string.error_password_not_equal)
        }

    private fun getPasswordErrorStatus(errorStatus: PasswordValidationStatus): String? =
        when (errorStatus) {
            PasswordValidationStatus.NOT_VALIDATED -> null
            PasswordValidationStatus.VALID -> null
            PasswordValidationStatus.NOT_CONTAINS_UPPER_CASE -> getString(R.string.error_not_contains_upper_case)
            PasswordValidationStatus.NOT_CONTAINS_LOWER_CASE -> getString(R.string.error_not_contains_lower_case)
            PasswordValidationStatus.NOT_CONTAINS_NUMBER -> getString(R.string.error_not_contains_number)
            PasswordValidationStatus.TO_SHORT -> getString(R.string.error_password_to_short)
            PasswordValidationStatus.EMPTY -> getString(R.string.error_empty)
        }

    private fun initOnFocusChangeListener() {
        binding.textInputFirstname.onFocusChangeListener =
            View.OnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    viewModel.validateFirstNameText(binding.textInputFirstname.text.toString())
                }
            }
        binding.textInputLastname.onFocusChangeListener = View.OnFocusChangeListener { _, isFocus ->
            if (!isFocus) {
                viewModel.validateLastNameText(binding.textInputLastname.text.toString())
            }
        }

        binding.textInputPassword.onFocusChangeListener = View.OnFocusChangeListener { _, isFocus ->
            if (!isFocus) {
                viewModel.validatePassword(binding.textInputPassword.text.toString())
            }
        }

        binding.textInputRepassword.onFocusChangeListener =
            View.OnFocusChangeListener { _, isFocus ->
                if (!isFocus) {
                    viewModel.validateRePassword(
                        binding.textInputPassword.text.toString(),
                        binding.textInputRepassword.text.toString()
                    )
                }
            }
    }

}