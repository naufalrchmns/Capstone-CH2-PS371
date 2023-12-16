package com.naufal.capstonewasteclassification.ui.scan

import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
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
import androidx.fragment.app.Fragment
import com.naufal.capstonewasteclassification.R
import com.naufal.capstonewasteclassification.databinding.FragmentScanBinding
import com.naufal.capstonewasteclassification.ml.Classification
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException

class ScanFragment : Fragment() {

    private val CAMERA_REQUEST_CODE = 123
    private val GALLERY_REQUEST_CODE = 456

    private lateinit var labels: List<String>
    private lateinit var bitmap: Bitmap
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imageView = binding.imageView7

        labels = requireActivity().assets.open("labels.txt").bufferedReader().readLines()

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        val takePhotoButton: Button = root.findViewById(R.id.button4)
        val choosePhotoButton: Button = root.findViewById(R.id.button5)
        val resultTextView: TextView = root.findViewById(R.id.textView2)
        val predictButton: Button = root.findViewById(R.id.button)

        takePhotoButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
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

            val processedImage = imageProcessor.process(tensorImage)

            try {
                val model = Classification.newInstance(requireContext())

                val inputFeature0 =
                    TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                inputFeature0.loadBuffer(processedImage.buffer)

                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                var maxIdx = 0
                outputFeature0.forEachIndexed { index, fl ->
                    if (outputFeature0[maxIdx] < fl) {
                        maxIdx = index
                    }
                }
                val result = labels[maxIdx]

                // Set the result to the TextView
                resultTextView.text = result

                model.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        // Add click listener to textView2
        resultTextView.setOnClickListener {
            if (::bitmap.isInitialized) {
                val result = resultTextView.text.toString()

                // Create an intent to perform a web search
                val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
                searchIntent.putExtra(SearchManager.QUERY, result)

                // Verify that there's an app that can handle the intent
                if (searchIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(searchIntent)
                } else {
                    // If no app found, inform the user
                    Toast.makeText(requireContext(), "No app found to handle the search", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please run prediction first", Toast.LENGTH_SHORT).show()
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
}