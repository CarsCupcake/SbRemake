package me.carscupcake.sbremake.util.quest;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TaskScheduler;

import java.util.ArrayList;
import java.util.List;

public class Dialog {
    private final String prefix;
    private final int delay;
    private final List<DialogElement> dialog = new ArrayList<>();

    public Dialog(String prefix, int delay) {
        this.prefix = prefix;
        this.delay = delay;
    }

    public Dialog addDialogElement(DialogElement element) {
        dialog.add(element);
        return this;
    }

    public Dialog addLine(String text) {
        return this.addLine(text, true);
    }

    public Dialog addLine(String text, boolean prefix) {
        return this.addDialogElement(new TextDialogElement((prefix ? this.prefix : "") + " " + (text)));
    }

    public DialogScheduler build(SkyblockPlayer player) {
        return new DialogScheduler(this, player);
    }

    public interface DialogElement {
        void send(DialogScheduler dialog, SkyblockPlayer player);
    }

    public static class DialogScheduler extends TaskScheduler {
        private final Dialog dialog;
        private final SkyblockPlayer player;
        private int i = 0;

        public DialogScheduler(Dialog dialog, SkyblockPlayer player) {
            this.dialog = dialog;
            this.player = player;
            if (player.getDialog() != null)
                throw new IllegalStateException("Player is in a dialog!");
            repeatTask(dialog.delay);
        }

        @Override
        public void run() {
            dialog.dialog.get(i).send(this, player);
            i++;
            if (i == dialog.dialog.size())
                cancel();
        }

        @Override
        public synchronized void cancel() {
            super.cancel();
            player.setDialog(null);
        }
    }

    public record TextDialogElement(String text) implements DialogElement {

        @Override
        public void send(DialogScheduler dialog, SkyblockPlayer player) {
            player.sendMessage(text);
        }
    }
}
