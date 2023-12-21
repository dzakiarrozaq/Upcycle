package com.bangkit.upcycle.camera

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bangkit.upcycle.R
import com.bangkit.upcycle.databinding.FragmentCameraBinding
import com.bangkit.upcycle.getImageUri
import com.bangkit.upcycle.ml.Model
import com.bangkit.upcycle.ml.Modelint8quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.text.Normalizer
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCameraBinding
    private lateinit var bitmap: Bitmap
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inputStream = requireContext().assets.open("labels.txt")
        val labels = inputStream.bufferedReader().readLines()
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        binding.galleryButton.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.cameraButton.setOnClickListener{ startCamera()}

        val imageProcess = ImageProcessor.Builder()
            .add(ResizeOp(224,224,ResizeOp.ResizeMethod.BILINEAR))
            .build()
        binding.uploadButton.setOnClickListener {
            currentImageUri?.let { uri ->
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)

                    var tensorImage = TensorImage(DataType.UINT8)
                    tensorImage.load(bitmap)
                    tensorImage = imageProcess.process(tensorImage)
                    val buffer = tensorImage.buffer
                    Log.d("After Normalization", "Min: ${buffer.float}, Max: ${buffer.float}")

                    Log.d("Image Dimensions", "Width: ${tensorImage.width}, Height: ${tensorImage.height}")

                    val model = Modelint8quant.newInstance(requireContext())
                    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                    inputFeature0.loadBuffer(tensorImage.buffer)
                    val shape = inputFeature0.shape
                    Log.d("Buffer Size", "Size: ${inputFeature0.buffer.capacity()} bytes")
                    Log.d("TensorBuffer Dimensions", "Shape: ${shape[0]} x ${shape[1]} x ${shape[2]} x ${shape[3]}")

                    val outputs = model.process(inputFeature0)
                    val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                    var maxIdx = 0
                    outputFeature0.forEachIndexed { index, fl ->
                        if (outputFeature0[maxIdx] < fl) {
                            maxIdx = index
                        }
                    }
                    if (maxIdx < labels.size) {
                        binding.tvname.text = labels[maxIdx].replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }
                        showPopUp()
                    } else {
                        binding.tvname.text = getString(R.string.has_no_label)
                        showPopUp()
                    }
                    model.close()
                } catch (e: Exception) {
                    Log.e("CameraFragment", "Error loading image: ${e.message}")
                }
            } ?: run {
                Log.e("CameraFragment", "No image selected")
            }
        }
        return binding.root
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }
    private fun showPopUp(){
        val popUp = Dialog(requireContext())
        popUp.setContentView(R.layout.popup)

        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(popUp.window?.attributes)
        layoutParams.width = width
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM or Gravity.START

        popUp.window?.attributes = layoutParams

        val radius = resources.getDimension(R.dimen.popup_corner_radius)
        val shape = GradientDrawable()
        shape.cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        popUp.window?.setBackgroundDrawable(shape)

        val backgroundView = popUp.findViewById<View>(R.id.backgroundView)
        val labelTextView = popUp.findViewById<TextView>(R.id.tvname1)

        backgroundView.setOnClickListener{
            popUp.dismiss()
        }
        labelTextView.text = binding.tvname.text
        popUp.show()
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CameraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CameraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}