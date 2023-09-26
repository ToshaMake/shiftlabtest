package com.example.myapplication.welcome.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.example.myapplication.welcome.presentation.WelcomeViewModel
import com.example.myapplication.databinding.FragmentWelcomBinding

class WelcomeFragment : Fragment(R.layout.fragment_welcom) {

    private companion object {
        const val FIRST_NAME_KEY = "firstName"
        const val REQUEST_KEY = "requestKey"
        const val PREFERENCE_NAME = "pref"
        const val BUNDLE_NAME = "name"
        const val DIALOG_TAG = "Dialog"
    }

    private lateinit var binding: FragmentWelcomBinding
    private lateinit var viewModel: WelcomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomBinding.inflate(inflater, container, false)
        setFragmentResultListener(REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getString(FIRST_NAME_KEY)
            viewModel = WelcomeViewModel(requireNotNull(result))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonHello.setOnClickListener {
            val bundle = bundleOf(BUNDLE_NAME to viewModel.getName())
            val dialogFragment = DialogFragment()
            dialogFragment.arguments = bundle
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            dialogFragment.show(transaction, DIALOG_TAG)
        }
        initOnBackPressedDispatcher()
    }

    private fun initOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val sharedPreference = requireContext().getSharedPreferences(
                        PREFERENCE_NAME,
                        Context.MODE_PRIVATE
                    )
                    sharedPreference.edit().apply() {
                        remove(FIRST_NAME_KEY)
                        apply()
                    }
                    Navigation.findNavController(binding.root)
                        .popBackStack()
                }
            })
    }

}