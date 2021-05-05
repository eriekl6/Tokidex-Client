package ca.tokidex.client.control;

import ca.tokidex.client.model.Tokimon;
import ca.tokidex.client.ui.WindowManager;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class holds the list of tokimons in memory
 * also responsible for contacting the web client to make calls to server before updating list
 */
public class Controller {

    private final TokimonWebClient tokimonWebClient = new TokimonWebClient();
    private WindowManager windowManager;
    private List<Tokimon> tokimonList = new ArrayList<>();

    public void getAllTokimon() {
        refreshList();
        Scene scene = windowManager.getDisplayAllTokiScene(tokimonList);
        windowManager.changePrimaryScene(scene);
    }

    public Tokimon getTokimonById(Long id) {
        return tokimonWebClient.getTokimonByIdCall(id);
    }

    public int addNewTokimon(Tokimon tokimon) {
        int responseCode = tokimonWebClient.addNewTokimonCall(tokimon);
        refreshList();
        return responseCode;
    }

    public int changeTokimon(Tokimon tokimon) {
        int responseCode = tokimonWebClient.changeTokimonByIdCall(tokimon);
        refreshList();
        return responseCode;
    }

    public void deleteTokimonById(Long id) {
        tokimonWebClient.deleteTokimonByIdCall(id);
        refreshList();
    }

    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    private void refreshList() {
        tokimonList = tokimonWebClient.getAllTokimonsCall();
    }
}
