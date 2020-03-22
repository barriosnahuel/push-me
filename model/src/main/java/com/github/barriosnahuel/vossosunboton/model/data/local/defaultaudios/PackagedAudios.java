package com.github.barriosnahuel.vossosunboton.model.data.local.defaultaudios;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.barriosnahuel.vossosunboton.model.R;
import com.github.barriosnahuel.vossosunboton.model.Sound;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
@SuppressWarnings("CPD-START")
public final class PackagedAudios {

    private PackagedAudios() {
        // Do nothing.
    }

    /**
     * @param context The execution context.
     * @return a list of sample audios packaged within the app.
     */
    public static List<Sound> get(@NonNull final Context context) {
        final List<Sound> files = new ArrayList<>();

        files.add(new Sound(context.getString(R.string.model_sample_button_activar_edited), R.raw.model_sample_button_podemosactivar));
        files.add(new Sound(context.getString(R.string.model_sample_button_activar), R.raw.model_sample_button_activar));
        files.add(new Sound(context.getString(R.string.model_sample_button_ambulancia), R.raw.model_sample_button_ambulancia));
        files.add(new Sound(context.getString(R.string.model_sample_button_banfield), R.raw.model_sample_button_banfield));
        files.add(new Sound(context.getString(R.string.model_sample_button_campito), R.raw.model_sample_button_campito));
        files.add(new Sound(context.getString(R.string.model_sample_button_careta), R.raw.model_sample_button_careta));
        files.add(new Sound(context.getString(R.string.model_sample_button_chimichimi), R.raw.model_sample_button_chimichimi));
        files.add(new Sound(context.getString(R.string.model_sample_button_chiquichiqui), R.raw.model_sample_button_chiquichiqui));
        files.add(new Sound(context.getString(R.string.model_sample_button_corrientes), R.raw.model_sample_button_corrientes));
        files.add(new Sound(context.getString(R.string.model_sample_button_crosscountry), R.raw.model_sample_button_crosscountry));
        files.add(new Sound(context.getString(R.string.model_sample_button_derecha), R.raw.model_sample_button_derecha));
        files.add(new Sound(context.getString(R.string.model_sample_button_dondeestan), R.raw.model_sample_button_dondeestan));
        files.add(new Sound(context.getString(R.string.model_sample_button_dondeestan2), R.raw.model_sample_button_dondeestan2));
        files.add(new Sound(context.getString(R.string.model_sample_button_dondeestas), R.raw.model_sample_button_dondeestas));
        files.add(new Sound(context.getString(R.string.model_sample_button_estamosyendo), R.raw.model_sample_button_estamosyendo));
        files.add(new Sound(context.getString(R.string.model_sample_button_guardaconcramer), R.raw.model_sample_button_guardaconcramer));
        files.add(new Sound(context.getString(R.string.model_sample_button_guardaeltaxi), R.raw.model_sample_button_guardaeltaxi));
        files.add(new Sound(context.getString(R.string.model_sample_button_izquierda), R.raw.model_sample_button_izquierda));
        files.add(new Sound(context.getString(R.string.model_sample_button_izquierdaizquierda), R.raw.model_sample_button_izquierdaizquierda));
        files.add(new Sound(context.getString(R.string.model_sample_button_lacamionetaimposible), R.raw.model_sample_button_lacamionetaimposible));
        files.add(new Sound(context.getString(R.string.model_sample_button_jajaja), R.raw.model_sample_button_jajajaja));
        files.add(new Sound(context.getString(R.string.model_sample_button_meseguis), R.raw.model_sample_button_meseguis));
        files.add(new Sound(context.getString(R.string.model_sample_button_nahu), R.raw.model_sample_button_nahuuu));
        files.add(new Sound(context.getString(R.string.model_sample_button_nochedesexo), R.raw.model_sample_button_nochedesexo));
        files.add(new Sound(context.getString(R.string.model_sample_button_ochominutos), R.raw.model_sample_button_llegamosenochominutos));
        files.add(new Sound(context.getString(R.string.model_sample_button_otrotema), R.raw.model_sample_button_otrotema));
        files.add(new Sound(context.getString(R.string.model_sample_button_pampayvinedos), R.raw.model_sample_button_pampayvinedos));
        files.add(new Sound(context.getString(R.string.model_sample_button_parademandarmsjs), R.raw.model_sample_button_parademandarmsjs));
        files.add(new Sound(context.getString(R.string.model_sample_button_pasaloenverdeoamarillo), R.raw.model_sample_button_pasaloverdeoamarillo));
        files.add(new Sound(context.getString(R.string.model_sample_button_pelotudomarchaatras), R.raw.model_sample_button_pelotudomarchaatras));
        files.add(new Sound(context.getString(R.string.model_sample_button_pepa), R.raw.model_sample_button_pepa));
        files.add(new Sound(context.getString(R.string.model_sample_button_pickypicky), R.raw.model_sample_button_pickypicky));
        files.add(new Sound(context.getString(R.string.model_sample_button_pistera), R.raw.model_sample_button_pistera));
        files.add(new Sound(context.getString(R.string.model_sample_button_poracahastaelsemaforo), R.raw.model_sample_button_poracahastaelsemaforo));
        files.add(new Sound(context.getString(R.string.model_sample_button_poronga), R.raw.model_sample_button_poronga));
        files.add(new Sound(context.getString(R.string.model_sample_button_quepasaconnahu), R.raw.model_sample_button_quepasaconnahu));
        files.add(new Sound(context.getString(R.string.model_sample_button_quierofiesta), R.raw.model_sample_button_quierofiesta));
        files.add(new Sound(context.getString(R.string.model_sample_button_radio), R.raw.model_sample_button_radio));
        files.add(new Sound(context.getString(R.string.model_sample_button_tresminutos), R.raw.model_sample_button_tresminutos));
        files.add(new Sound(context.getString(R.string.model_sample_button_todosimulado), R.raw.model_sample_button_todosimulado));
        files.add(new Sound(context.getString(R.string.model_sample_button_trescientosocholacon), R.raw.model_sample_button_trecientosocholacon));
        files.add(new Sound(context.getString(R.string.model_sample_button_unahorahaciendoluces), R.raw.model_sample_button_unahorahaciendoluces));
        files.add(new Sound(context.getString(R.string.model_sample_button_verdeoamarillo), R.raw.model_sample_button_verdeoamarillo));

        return files;
    }
}
