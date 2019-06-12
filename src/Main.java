import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        Steganography steganography = new Steganography();

        String imagePath = "./imgs/blackhole.jpg";
        String imageDecodePath = "./imgs/blackholeSteg.png";

        boolean encodeResult = false;
        try {
            encodeResult = steganography.encode(imagePath, "Je suis un texte caché!");
        } catch (IOException e) {
            System.out.println("Error, " + imagePath + " could not be read !");
        }

        System.out.println("Encode : " + encodeResult);
        String message = steganography.decode(imageDecodePath);

        System.out.println(message);
    }
}
