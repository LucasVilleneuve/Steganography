import java.io.File;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        Steganography steganography = new Steganography();

        String imageDecodePath = "./imgs/blackholeSteg.png";

        FilePicker filePicker = new FilePicker();

        String imagePath = filePicker.pickFile();
        if (imagePath == null) return;

        boolean encodeResult = false;
        try {
            encodeResult = steganography.encode(imagePath, "Je suis un trou caché!");
        } catch (IOException e) {
            System.out.println("Error, " + imagePath + " could not be read !");
        }

        System.out.println("Encode : " + encodeResult);
        if (encodeResult)
        {
            try
            {
                String message = steganography.decode(imageDecodePath);
                System.out.println(message);
            } catch (IOException e) {
                System.out.println("Error, " + imagePath + " could not be read !");
            }
        }

    }
}
