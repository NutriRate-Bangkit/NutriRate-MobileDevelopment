package com.example.nutrirateapp.view.profile

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.DialogEditProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditProfileDialogFragment : DialogFragment() {
    private var _binding: DialogEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(requireContext())
                .load(it)
                .circleCrop()
                .placeholder(R.drawable.img_user)
                .error(R.drawable.img_user)
                .into(binding.imgProfile)
        } ?: Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = DialogEditProfileBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        setupWindowAttributes(dialog)
        setupListeners()
        setupObservers()
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
            cdvPhoto.setOnClickListener {
                it.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                    }
                    .start()

                checkPermissionAndOpenGallery()
            }

            btnCancel.setOnClickListener {
                it.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()

                        dismiss()
                    }
                    .start()
            }

            btnSave.setOnClickListener {
                it.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()

                        handleProfileUpdate()
                    }
                    .start()
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnSave.isEnabled = !isLoading
            binding.btnCancel.isEnabled = !isLoading
        }

        viewModel.updateProfileResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                result.fold(
                    onSuccess = { response ->
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        viewModel.getProfile()
                        dismiss()
                    },
                    onFailure = { exception ->
                        val errorMessage = when {
                            exception.message?.contains("Format file tidak didukung") == true ->
                                "Format file tidak didukung"
                            exception.message?.contains("Token") == true ->
                                "Sesi telah berakhir"
                            else -> "Terjadi kesalahan pada server"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
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

                        Glide.with(requireContext())
                            .load(profile.image)
                            .circleCrop()
                            .placeholder(R.drawable.img_user)
                            .error(R.drawable.img_user)
                            .into(imgProfile)
                    }
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Failed to load profile: ${exception.message}",
                        Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            )
        }
    }

    private fun checkPermissionAndOpenGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionExplanationDialog()
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    private fun showPermissionExplanationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Storage permission is required to select profile picture")
            .setPositiveButton("Grant") { _, _ ->
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSIONS
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun handleProfileUpdate() {
        val name = binding.etEditName.text.toString()
        val email = binding.etEditEmail.text.toString()

        if (name.isEmpty() && email.isEmpty() && selectedImageUri == null) {
            Toast.makeText(context, "Tidak ada data yang diperbarui", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updateProfile(
            name = if (name.isNotEmpty()) name else null,
            email = if (email.isNotEmpty()) email else null,
            imageUri = selectedImageUri
        )
    }

    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        viewModel.updateProfileResult.removeObservers(this)
        viewModel.profileResult.removeObservers(this)
        viewModel.isLoading.removeObservers(this)
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}