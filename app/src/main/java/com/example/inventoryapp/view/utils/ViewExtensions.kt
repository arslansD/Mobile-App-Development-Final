package com.example.inventoryapp.view.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.navigate(directions: NavDirections) {
    val controller = findNavController()
    val currentDestination = (controller.currentDestination as? FragmentNavigator.Destination)?.className
        ?: (controller.currentDestination as? DialogFragmentNavigator.Destination)?.className
    if (currentDestination == this.javaClass.name) {
        controller.navigate(directions)
    }
}

fun String.showToast(context: Context){
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

fun ImageView.loadImage(image:Bitmap){
    Glide.with(this.context)
        .load(image)
        .into(this)
}

fun Fragment.showBottomSheetDialog(
    @LayoutRes layout: Int,
    fullScreen: Boolean = true,
    expand: Boolean = true
) {
    val dialog = BottomSheetDialog(context!!)
    dialog.setOnShowListener {
        val bottomSheet: FrameLayout = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        if (fullScreen && bottomSheet.layoutParams != null) { showFullScreenBottomSheet(bottomSheet) }

        if (!expand) return@setOnShowListener

        bottomSheet.setBackgroundResource(android.R.color.transparent)
        expandBottomSheet(bottomSheetBehavior)
    }

    @SuppressLint("InflateParams") // dialog does not need a root view here
    val sheetView = layoutInflater.inflate(layout, null)

    dialog.setContentView(sheetView)
    dialog.show()
}

private fun showFullScreenBottomSheet(bottomSheet: FrameLayout) {
    val layoutParams = bottomSheet.layoutParams
    layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
    bottomSheet.layoutParams = layoutParams
}

private fun expandBottomSheet(bottomSheetBehavior: BottomSheetBehavior<FrameLayout>) {
    bottomSheetBehavior.skipCollapsed = true
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
}