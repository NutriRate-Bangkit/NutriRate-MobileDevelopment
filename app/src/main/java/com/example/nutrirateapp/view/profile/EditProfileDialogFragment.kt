package com.example.nutrirateapp.view.profile

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.DialogEditProfileBinding

class EditProfileDialogFragment : DialogFragment() {
    private var _binding: DialogEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                binding.imgProfile.setImageURI(uri)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = DialogEditProfileBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        setupWindowAttributes(dialog)
        setupListeners()
        loadCurrentProfile()

        return dialog
    }

    private fun setupWindowAttributes(dialog: Dialog) {
        dialog.window?.apply {
            val params: WindowManager.LayoutParams = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
            setBackgroundDrawableResource(android.R.color.transparent)
            setGravity(Gravity.CENTER)

            decorView.setPadding(
                convertDpToPx(16),
                convertDpToPx(16),
                convertDpToPx(16),
                convertDpToPx(16)
            )
        }
    }

    private fun setupListeners() {
        with(binding) {
            // Handle image selection
            cdvPhoto.setOnClickListener {
                openImagePicker()
            }

            // Handle Cancel Button
            btnCancel.setOnClickListener {
                dismiss()
            }

            // Handle Save Button
            btnSave.setOnClickListener {
                updateProfile()
            }
        }
    }

    private fun loadCurrentProfile() {
        viewModel.profileResult.observe(this) { result ->
            result.fold(
                onSuccess = { profile ->
                    binding.apply {
                        etEditName.setText(profile.name)
                        etEditEmail.setText(profile.email)

                        // Load current profile image
                        Glide.with(requireContext())
                            .load(profile.image)
                            .circleCrop()
                            .placeholder(R.drawable.img_user)
                            .error(R.drawable.img_user)
                            .into(imgProfile)
                    }
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Failed to load profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            )
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnSave.isEnabled = !isLoading
        }

        // Observe update result
        viewModel.updateProfileResult.observe(this) { result ->
            result.fold(
                onSuccess = { response ->
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    viewModel.getProfile() // Refresh profile data
                    dismiss()
                },
                onFailure = { exception ->
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    private fun updateProfile() {
        val name = binding.etEditName.text.toString()
        val email = binding.etEditEmail.text.toString()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updateProfile(name, email, selectedImageUri)
    }

    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}