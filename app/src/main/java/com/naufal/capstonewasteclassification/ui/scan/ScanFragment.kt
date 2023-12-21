package com.naufal.capstonewasteclassification.ui.scan

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.naufal.capstonewasteclassification.R
import com.naufal.capstonewasteclassification.auth.register.RegisterActivity
import com.naufal.capstonewasteclassification.databinding.FragmentScanBinding
import com.naufal.capstonewasteclassification.ml.Classification
import com.naufal.capstonewasteclassification.ui.home.HomeFragment
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScanFragment : Fragment() {

    private val CAMERA_REQUEST_CODE = 123
    private val GALLERY_REQUEST_CODE = 456
    val imageSize = 256

    private lateinit var labels: List<String>
    private lateinit var bitmap: Bitmap
    private lateinit var imageView: ImageView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imageView = binding.imageView7

        labels = requireActivity().assets.open("labels.txt").bufferedReader().readLines()

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


//        val imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
//            .build()

        val takePhotoButton: Button = root.findViewById(R.id.button4)
        val choosePhotoButton: Button = root.findViewById(R.id.button5)
        val resultTextView: TextView = root.findViewById(R.id.textView2)
        val predictButton: Button = root.findViewById(R.id.button)
        val next: Button = root.findViewById(R.id.btn_next)

        takePhotoButton.setOnClickListener {
            checkCameraPermission()
        }

        choosePhotoButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"

            startActivityForResult(
                Intent.createChooser(intent, "Select an Image"),
                GALLERY_REQUEST_CODE
            )
        }

        predictButton.setOnClickListener {
            if (!::bitmap.isInitialized) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tensorImage = TensorImage(DataType.UINT8)
            tensorImage.load(bitmap)

            try {
                val model = Classification.newInstance(requireContext())

                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
                var byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
                byteBuffer.order(ByteOrder.nativeOrder())

                val intValues = IntArray(imageSize * imageSize)
                bitmap!!.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
                var pixel = 0
                for (i in 0 until imageSize) {
                    for (j in 0 until imageSize) {
                        val `val` = intValues[pixel++] // RGB
                        byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                        byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                        byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
                    }
                }
                inputFeature0.loadBuffer(byteBuffer)

                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer

                val confidences = outputFeature0.floatArray

                val threshold = 0.5f // Adjust this threshold as needed
                val predictedLabel = if (confidences[0] >= threshold) "Recycle" else "Organic"

                var maxPos = 0
                var maxConfidence = 0f
                for (i in confidences.indices) {
                    if (confidences[i] > maxConfidence) {
                        maxConfidence = confidences[i]
                        maxPos = i
                    }
                }


                val result = predictedLabel

                // Set the result to the TextView
                resultTextView.text = result

                model.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val image = data?.extras?.get("data") as? Bitmap
                    if (image != null) {
                        setImage(image)
                    } else {
                        Toast.makeText(requireContext(), "Failed to retrieve image", Toast.LENGTH_SHORT).show()
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    val uri = data?.data
                    try {
                        val image = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                        setImage(image)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun setImage(image: Bitmap) {
        // Convert the bitmap to ARGB_8888 format
        val argbBitmap = image.copy(Bitmap.Config.ARGB_8888, true)

        // Set the converted Bitmap to the class variable
        bitmap = argbBitmap

        // Set the converted Bitmap to the ImageView
        imageView.setImageBitmap(bitmap)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            // Permission is already granted, launch the camera
            startCamera()
        }
    }


    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, launch the camera
                    startCamera()
                } else {
                    // Camera permission denied, show a message or handle accordingly
                    Toast.makeText(
                        requireContext(),
                        "Camera permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}