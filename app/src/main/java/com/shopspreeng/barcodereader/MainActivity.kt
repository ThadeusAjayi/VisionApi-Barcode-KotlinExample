package com.shopspreeng.barcodereader

import android.Manifest.permission.*
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat


class MainActivity : AppCompatActivity(), BarcodeFragment.OnFragmentInteractionListener {

    var file : File? = null
    private var mCurrentPhotoPath : String = ""
    var uri: Uri? = null
    var fragment : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener({


            if (ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions(this,
                        arrayOf<String>(CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
                        0)
            } else {

                if (!fragment) {
                    fragment = true
                    supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, BarcodeFragment())
                            .addToBackStack(null)
                            .commit()
                }

            }
        })

    }

    override fun onBackPressed() {
        fragment = false
        super.onBackPressed()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        mCurrentPhotoPath = image.absolutePath
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.size == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                Toast.makeText(this, "Permission granted start camera", Toast.LENGTH_SHORT).show()
            } else {
                // Permission was denied or request was cancelled
                Toast.makeText(this, "Sorry we can't continue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
