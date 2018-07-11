package com.shopspreeng.barcodereader

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.Barcode.DATA_MATRIX
import com.google.android.gms.vision.barcode.Barcode.QR_CODE
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_blank.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BarcodeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class BarcodeFragment : Fragment(), SurfaceHolder.Callback, Detector.Processor<Barcode> {

    private var mListener: OnFragmentInteractionListener? = null

    lateinit var cameraSource : CameraSource
    var surfaceHolder : SurfaceHolder? = null
    lateinit var cameraView : SurfaceView


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_blank, container, false)

        cameraView = rootView.findViewById<SurfaceView>(R.id.camera_view)
        cameraView.setZOrderMediaOverlay(true)

        surfaceHolder = cameraView.holder

        val barcodeDetector = BarcodeDetector.Builder(activity)
                .setBarcodeFormats(DATA_MATRIX or QR_CODE)
                .build()

        if (!barcodeDetector.isOperational) {
            Toast.makeText(activity, "Could not set up the detector!", Toast.LENGTH_SHORT).show()
            activity.onBackPressed()
        } else {
            cameraSource = CameraSource.Builder(context, barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedFps(24F)
                    .setAutoFocusEnabled(true)
                    .setRequestedPreviewSize(1920, 1024)
                    .build()

            cameraView.holder.addCallback(this)
        }

        barcodeDetector.setProcessor(this)

        return rootView
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        cameraSource.stop()
    }

    @SuppressLint("MissingPermission")
    override fun surfaceCreated(p0: SurfaceHolder?) {
        cameraSource.start(cameraView.holder)
    }

    override fun release() {
    }

    override fun receiveDetections(detections : Detector.Detections<Barcode>?) {
        val barcodes = detections!!.detectedItems
            Toast.makeText(activity, barcodes.valueAt(0).toString(), Toast.LENGTH_SHORT).show()

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
