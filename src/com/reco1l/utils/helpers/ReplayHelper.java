package com.reco1l.utils.helpers;

import static androidx.activity.result.contract.ActivityResultContracts.*;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.edlplan.replay.OdrDatabase;
import com.edlplan.replay.OsuDroidReplay;
import com.edlplan.replay.OsuDroidReplayPack;
import com.reco1l.Game;
import com.reco1l.UI;
import com.reco1l.data.GameNotification;
import com.reco1l.tables.DialogTable;
import com.reco1l.tables.Res;
import com.reco1l.ui.custom.Dialog;
import com.reco1l.ui.custom.DialogBuilder;

import java.io.File;
import java.util.List;

import ru.nsu.ccfit.zuev.osuplus.BuildConfig;
import ru.nsu.ccfit.zuev.osuplus.R;

public final class ReplayHelper {

    //--------------------------------------------------------------------------------------------//

    private ReplayHelper() {
    }

    //--------------------------------------------------------------------------------------------//

    public static void export(int pId) {
        List<OsuDroidReplay> replays = OdrDatabase.get().getReplayById(pId);

        GameNotification notification = GameNotification.of("replay export")
                .setMessage("Exporting replay...")
                .showProgress(true);

        notification.commit();
        try {
            OsuDroidReplay replay = replays.get(0);
            File parent = new File(Environment.getExternalStorageDirectory(), "osu!droid/export");

            CharSequence name = replay.getFileName().subSequence(
                    replay.getFileName().indexOf('/') + 1,
                    replay.getFileName().lastIndexOf('.')
            );

            File file = new File(parent, String.format("%s [%s]-%d.edr", name, replay.getPlayerName(), replay.getTime()));

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            OsuDroidReplayPack.packTo(file, replay);

            notification.runOnClick(() -> {

                Uri uri = FileProvider.getUriForFile(Game.activity, BuildConfig.APPLICATION_ID + ".fileProvider", file);

                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setPackage("com.edlplan.ui")
                        .setDataAndType(uri, "*/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Game.activity.registerForActivityResult(new StartActivityForResult(), result -> {
                        })
                        .launch(intent);
            });

            notification.setMessage("Saved replay to " + file.getAbsolutePath());
        } catch (Exception exception) {
            notification.setMessage("Failed to export replay!\n" + exception.getMessage())
                    .runOnClick(() ->
                            new Dialog(DialogTable.stacktrace(exception)).show()
                    );
        }
    }

    public static void delete(int pId) {
        DialogBuilder builder = new DialogBuilder("Delete Replay")
                .setMessage("Are you sure?")
                .addButton("Yes", d -> {

                    GameNotification notification = GameNotification.of("replay delete")
                            .setMessage("Deleting score " + pId + "...")
                            .showProgress(true);

                    try {
                        if (OdrDatabase.get().deleteReplay(pId) != 0) {
                            notification.setMessage("Score " + pId + " has been successfully deleted");
                        } else {
                            notification.setMessage("Score " + pId + " was not found or database is null!")
                                    .showProgress(false)
                                    .showCloseButton(true);
                        }
                    } catch (Exception e) {
                        notification
                                .setMessage("Failed to delete replay!\n" + e.getMessage())
                                .runOnClick(() ->
                                        new Dialog(DialogTable.stacktrace(e)).show()
                                );
                    }
                    d.close();
                });

        new Dialog(builder).show();
    }
}
