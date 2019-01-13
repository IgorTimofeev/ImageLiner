package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
	public ImageView imageView;
	public Slider slider;
	public Label label;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void updateLabel() {
    	label.setText("Line step: " + (int) slider.getValue());
	}
    
    public void initialize() {
    	slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			updateLabel();
		});

		updateLabel();
	}

	//Ебучий драг-дроп
	public void onDragEntered(DragEvent event) {
		System.out.println("ENTERED");
		if (event.getDragboard().hasFiles())
			event.acceptTransferModes(TransferMode.COPY);
	}
	
	public void onDragExited(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if (dragboard.hasFiles()) {
			File file = dragboard.getFiles().get(0);

			String fileName = file.getName();
			int dotIndex = fileName.lastIndexOf('.');
			if (dotIndex > 0) {
				String extension = fileName.substring(dotIndex + 1);
				if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")) {
					try {
						BufferedImage image = ImageIO.read(file);

						int step = (int) slider.getValue();
						for (int j = 0; j < image.getHeight(); j += step * 2)
							for (int i = j; i < j + step; i++)
								if (i < image.getHeight())
									for (int k = 0; k < image.getWidth(); k++)
										image.setRGB(k, i, 0x0);

						imageView.setImage(SwingFXUtils.toFXImage(image, null));
						ImageIO.write(image, extension, new File(file.getParentFile(), fileName.substring(0, dotIndex) + " (lines)." + extension));
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
