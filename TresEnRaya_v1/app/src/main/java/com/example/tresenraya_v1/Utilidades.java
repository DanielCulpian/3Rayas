package com.example.tresenraya_v1;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class Utilidades {

    private static Toast toast;

    public static void mostrarToast(Context context, String mensaje, int duracion) {
        // Cancela el Toast anterior si existe
        if (toast != null) {
            toast.cancel();
        }

        // Muestra el nuevo Toast
        toast = Toast.makeText(context, mensaje, duracion);
        toast.show();
    }

    public static void mostrarToastConRetraso(Context context, String mensaje1, int duracion1, String mensaje2, int duracion2, long retraso) {
        mostrarToast(context, mensaje1, duracion1);

        // Programa la ejecución del segundo Toast después del retraso especificado
        new Handler().postDelayed(() -> mostrarToast(context, mensaje2, duracion2), retraso);
    }
}

