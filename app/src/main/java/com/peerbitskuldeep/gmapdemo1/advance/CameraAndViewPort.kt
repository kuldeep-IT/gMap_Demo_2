package com.peerbitskuldeep.gmapdemo1.advance

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class CameraAndViewPort {

    val surat: CameraPosition = CameraPosition.builder()
        .target(LatLng(21.19858056099887, 72.85978032049988))
        .zoom(17f)
        .bearing(0f)
        .tilt(45f)
        .build()
}