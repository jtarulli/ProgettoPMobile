package com.example.progettopm

import android.app.AlertDialog
import android.content.Context

object DeleteConfirmationDialog {
    fun showConfirmationDialog(
        context: Context?,
        title: String?,
        message: String?,
        listener: OnConfirmListener?
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                "Conferma"
            ) { dialog, which -> // Azione conferma
                listener?.onConfirm()
            }
            .setNegativeButton(
                "Annulla"
            ) { dialog, which -> // Azione annulla
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    interface OnConfirmListener {
        fun onConfirm()
    }
}