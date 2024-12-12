package com.example.nutrirateapp.view.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.example.nutrirateapp.R
import com.example.nutrirateapp.view.main.MainActivity
import com.example.nutrirateapp.view.result.ResultActivity

@ExperimentalGetImage
class CameraActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var captureButton: ImageButton
    private lateinit var galleryButton: ImageButton
    private lateinit var switchCameraButton: ImageButton
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private var isUsingBackCamera = true // Untuk melacak status kamera (depan atau belakang)

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                processImageFromGallery(it)
            } ?: Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Inisialisasi view
        previewView = findViewById(R.id.cameraPreview)
        captureButton = findViewById(R.id.captureButton)
        galleryButton = findViewById(R.id.galleryButton)
        switchCameraButton = findViewById(R.id.switchCameraButton)

        // Periksa izin kamera
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


        captureButton.setOnClickListener {
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

            captureImageAndProcessText()
        }
        
        galleryButton.setOnClickListener {
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

            openGallery()
        }

        switchCameraButton.setOnClickListener {
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

            switchCamera()
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        // Pilih kamera berdasarkan status isUsingBackCamera
        val cameraSelector = if (isUsingBackCamera) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }

        // Konfigurasi Preview
        val preview = Preview.Builder()
            .setTargetResolution(Size(1920, 1080))
            .setTargetRotation(previewView.display.rotation)
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        // Konfigurasi ImageCapture
        imageCapture = ImageCapture.Builder()
            .setTargetResolution(Size(1920, 1080))
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetRotation(previewView.display.rotation)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
        } catch (exc: Exception) {
            Toast.makeText(this, "Failed to bind camera use cases: ${exc.message}", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "bindCameraUseCases: ${exc.message}", exc)
        }
    }

    private fun switchCamera() {
        isUsingBackCamera = !isUsingBackCamera
        bindCameraUseCases()
    }

    private fun captureImageAndProcessText() {
        if (!::imageCapture.isInitialized) {
            Toast.makeText(this, "Camera not initialized", Toast.LENGTH_SHORT).show()
            return
        }

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val mediaImage = image.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)

                        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                        recognizer.process(inputImage)
                            .addOnSuccessListener { visionText ->
                                Log.d(TAG, "Detected text: ${visionText.text}")
                                processNutritionText(visionText.text)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this@CameraActivity, "Failed to recognize text", Toast.LENGTH_SHORT).show()
                                Log.e(TAG, "Error detecting text: ${e.message}", e)
                            }
                            .addOnCompleteListener {
                                image.close()
                            }
                    } else {
                        Toast.makeText(this@CameraActivity, "Failed to retrieve image", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Image capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun processImageFromGallery(uri: Uri) {
        try {
            val inputImage = InputImage.fromFilePath(this, uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    Log.d(TAG, "Detected text: ${visionText.text}")
                    processNutritionText(visionText.text)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to recognize text", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error detecting text: ${e.message}", e)
                }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to process image: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error processing image: ${e.message}", e)
        }
    }

    private fun processNutritionText(detectedText: String) {
        val servingSizeRegex = Regex("(?i)Takaran Saji\\s*(\\d+(\\.\\d+)?)(\\s*[a-zA-Z]*)")
        val proteinRegex = Regex("(?i)Protein\\s*(\\d+(\\.\\d+)?)(\\s*[a-zA-Z]*)")
        val energyRegex = Regex("(?i)Energi\\s*(\\d+(\\.\\d+)?)(\\s*[a-zA-Z]*)")
        val fatRegex = Regex("(?i)Lemak\\s*(\\d+(\\.\\d+)?)(\\s*[a-zA-Z]*)")
        val saturatedFatRegex = Regex("(?i)Lemak Jenuh\\s*(\\d+(\\.\\d+)?)(\\s*[a-zA-Z]*)")
        val sugarRegex = Regex("(?i)Gula\\s*(\\d+(\\.\\d+)?)(\\s*[a-zA-Z]*)")
        val fiberRegex = Regex("(?i)Serat Pangan\\s*(\\d+(\\.\\d+)?)(\\s*[a-zA-Z]*)")
        val sodiumRegex = Regex("(?i)Natrium\\s*(\\d+(\\.\\d+)?)(\\s*[a-zA-Z]*)")

        val servinSize = servingSizeRegex.find(detectedText)?.groups?.get(1)?.value ?: ""
        val protein = proteinRegex.find(detectedText)?.groups?.get(1)?.value ?: ""
        val energy = energyRegex.find(detectedText)?.groups?.get(1)?.value ?: ""
        val fat = fatRegex.find(detectedText)?.groups?.get(1)?.value ?: ""
        val saturatedFat = saturatedFatRegex.find(detectedText)?.groups?.get(1)?.value ?: ""
        val sugar = sugarRegex.find(detectedText)?.groups?.get(1)?.value ?: ""
        val fiber = fiberRegex.find(detectedText)?.groups?.get(1)?.value ?: ""
        val sodium = sodiumRegex.find(detectedText)?.groups?.get(1)?.value ?: ""

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SERVING", servinSize)
        intent.putExtra("PROTEIN", protein)
        intent.putExtra("ENERGY", energy)
        intent.putExtra("FAT", fat)
        intent.putExtra("SATURATED_FAT", saturatedFat)
        intent.putExtra("SUGAR", sugar)
        intent.putExtra("FIBER", fiber)
        intent.putExtra("SODIUM", sodium)
        startActivity(intent)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()

        super.onBackPressed()
    }



    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}