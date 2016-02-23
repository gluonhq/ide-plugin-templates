package ${packageName}.view;

import com.gluonhq.particle.annotation.ParticleView;
import com.gluonhq.particle.view.View;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

@ParticleView(name = "basic", isDefault = true)
public class BasicView implements View {
    
    private final VBox controls = new VBox(15);

    @Override
    public void init() {
        Label label = new Label("Hello JavaFX World!");

        Button button = new Button("Change the World!");
        button.setOnAction(e -> label.setText("Hello JavaFX Universe!"));
        
        controls.getChildren().addAll(label, button);
        controls.setAlignment(Pos.CENTER);
    }

    @Override
    public Node getContent() {
        return controls;
    }

}
