package com.github.barriosnahuel.vossosunboton.data.local.defaultaudios;

import android.content.Context;
import android.support.annotation.NonNull;
import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.data.model.Sound;
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

    public static List<Sound> get(@NonNull final Context context) {
        final List<Sound> files = new ArrayList<>();

        files.add(new Sound(context.getString(R.string.activar), R.raw.activar));
        files.add(new Sound(context.getString(R.string.activar), R.raw.podemosactivar));
        files.add(new Sound(context.getString(R.string.ambulancia), R.raw.ambulancia));
        files.add(new Sound(context.getString(R.string.banfield), R.raw.banfield));
        files.add(new Sound(context.getString(R.string.campito), R.raw.campito));
        files.add(new Sound(context.getString(R.string.careta), R.raw.careta));
        files.add(new Sound(context.getString(R.string.chimichimi), R.raw.chimichimi));
        files.add(new Sound(context.getString(R.string.chiquichiqui), R.raw.chiquichiqui));
        files.add(new Sound(context.getString(R.string.corrientes), R.raw.corrientes));
        files.add(new Sound(context.getString(R.string.crosscountry), R.raw.crosscountry));
        files.add(new Sound(context.getString(R.string.derecha), R.raw.derecha));
        files.add(new Sound(context.getString(R.string.dondeestan), R.raw.dondeestan));
        files.add(new Sound(context.getString(R.string.dondeestan2), R.raw.dondeestan2));
        files.add(new Sound(context.getString(R.string.dondeestas), R.raw.dondeestas));
        files.add(new Sound(context.getString(R.string.estamosyendo), R.raw.estamosyendo));
        files.add(new Sound(context.getString(R.string.guardaconcramer), R.raw.guardaconcramer));
        files.add(new Sound(context.getString(R.string.guardaeltaxi), R.raw.guardaeltaxi));
        files.add(new Sound(context.getString(R.string.izquierda), R.raw.izquierda));
        files.add(new Sound(context.getString(R.string.izquierdaizquierda), R.raw.izquierdaizquierda));
        files.add(new Sound(context.getString(R.string.lacamionetaimposible), R.raw.lacamionetaimposible));
        files.add(new Sound(context.getString(R.string.jajaja), R.raw.jajajaja));
        files.add(new Sound(context.getString(R.string.meseguis), R.raw.meseguis));
        files.add(new Sound(context.getString(R.string.nahu), R.raw.nahuuu));
        files.add(new Sound(context.getString(R.string.nochedesexo), R.raw.nochedesexo));
        files.add(new Sound(context.getString(R.string.ochominutos), R.raw.llegamosenochominutos));
        files.add(new Sound(context.getString(R.string.otrotema), R.raw.otrotema));
        files.add(new Sound(context.getString(R.string.pampayvinedos), R.raw.pampayvinedos));
        files.add(new Sound(context.getString(R.string.parademandarmsjs), R.raw.parademandarmsjs));
        files.add(new Sound(context.getString(R.string.pasaloenverdeoamarillo), R.raw.pasaloenverdeoamarillo));
        files.add(new Sound(context.getString(R.string.pelotudomarchaatras), R.raw.pelotudomarchaatras));
        files.add(new Sound(context.getString(R.string.pepa), R.raw.pepa));
        files.add(new Sound(context.getString(R.string.pickypicky), R.raw.pickypicky));
        files.add(new Sound(context.getString(R.string.pistera), R.raw.pistera));
        files.add(new Sound(context.getString(R.string.poracahastaelsemaforo), R.raw.poracahastaelsemaforo));
        files.add(new Sound(context.getString(R.string.poronga), R.raw.poronga));
        files.add(new Sound(context.getString(R.string.quepasaconnahu), R.raw.quepasaconnahu));
        files.add(new Sound(context.getString(R.string.quierofiesta), R.raw.quierofiesta));
        files.add(new Sound(context.getString(R.string.radio), R.raw.radio));
        files.add(new Sound(context.getString(R.string.tresminutos), R.raw.tresminutos));
        files.add(new Sound(context.getString(R.string.todosimulado), R.raw.todosimulado));
        files.add(new Sound(context.getString(R.string.trescientosocholacon), R.raw.trecientosocholacon));
        files.add(new Sound(context.getString(R.string.unahorahaciendoluces), R.raw.unahorahaciendoluces));
        files.add(new Sound(context.getString(R.string.verdeoamarillo), R.raw.verdeoamarillo));

        return files;
    }
}
