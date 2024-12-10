package com.capstone.batiklen.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.capstone.batiklen.databinding.FragmentCameraBinding
import com.capstone.batiklen.ui.result.ResultActivity
import com.capstone.batiklen.utils.createCustomTempFile
import com.yalantis.ucrop.UCrop

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private var currentImageUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val cameraViewModel =
//            ViewModelProvider(this).get(CameraViewModel::class.java)

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchCamera.setOnClickListener{
            cameraSelector =
                if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

        binding.takePicture.setOnClickListener{takePicture()}

        binding.pickGallery.setOnClickListener { pickFromGalley() }
    }

    private fun pickFromGalley() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){uri: Uri? ->
        if (uri != null){
            currentImageUri = uri

            val uCropOption = UCrop.Options()
            val source = currentImageUri!!
            val destinationFile = createCustomTempFile(requireContext(), "cropped")
            val destinationUri = Uri.fromFile(destinationFile)

            val uCrop = UCrop.of(source, destinationUri)
                .withOptions(uCropOption)
                .withMaxResultSize(1000,1000)

            registerUCrop.launch(uCrop.getIntent(requireContext()))
        }else{
            Log.d("photo Picker", "No Media Selected")
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    private val registerUCrop = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode == Activity.RESULT_OK){
            val data = UCrop.getOutput(result.data!!)

            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra("CAMERAXPIC", data.toString())
            startActivity(intent)
        }else if(result.resultCode == UCrop.RESULT_ERROR){
            val uCropErr = UCrop.getError(result.data!!)
            Log.d("UcropErr", "$uCropErr")
        }
    }



    private fun takePicture() {
        val imageCapture = imageCapture ?: return

        val pictureFile = createCustomTempFile(requireContext())

        val outputPicture = ImageCapture.OutputFileOptions.Builder(pictureFile).build()

        imageCapture.takePicture(outputPicture, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uCropOption = UCrop.Options()
                    val source = Uri.fromFile(pictureFile)
                    val destinationFile = createCustomTempFile(requireContext(), "cropped")
                    val destinationUri = Uri.fromFile(destinationFile)

                    val uCrop = UCrop.of(source, destinationUri)
                        .withOptions(uCropOption)
                        .withMaxResultSize(1000,1000)

                    registerUCrop.launch(uCrop.getIntent(requireContext()))
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("errorTakePicture", "message: $exception")
                }

            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    requireActivity(),
                    cameraSelector,
                    preview,
                    imageCapture
                )
            }catch (e: Exception){
                Toast.makeText(requireActivity(), "Gagal memunculkan Kamera", Toast.LENGTH_SHORT).show()
                Log.d("cameraActivity", "$e")
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}