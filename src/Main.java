import java.io.IOException;
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class Main
{

    public static void main(String[] args)
    {
        Steganography steganography = new Steganography();
        FilePicker filePicker = new FilePicker();
        String messageToConceal = "";

        Scanner in = new Scanner(System.in);

        System.out.println("Choose your option:");
        System.out.println("E - encode");
        System.out.println("D - decode");
        System.out.println("B - both");

        char input = toUpperCase(in.next().charAt(0));
        in.nextLine();

        if (input == 'E' || input == 'B')
        {
            System.out.println("Choose a message to conceal:");
            messageToConceal = in.nextLine();
        }

        if (input == 'B') // Encode and decode
        {
            String imagePath = filePicker.pickFile();
            if (imagePath == null) return;

            boolean encodeResult = encode(steganography, imagePath, messageToConceal);

            if (encodeResult) // If encode succeeded
            {
                String imageDecodePath = steganography.getLastSavedImageName();
                decode(steganography, imageDecodePath);
            }
        }
        else if (input == 'E') //  Encode
        {
            String imagePath = filePicker.pickFile();
            if (imagePath == null) return;

            encode(steganography, imagePath, messageToConceal);
        }
        else if (input == 'D') // Decode
        {
            String imagePath = filePicker.pickFile();
            if (imagePath == null) return;

            decode(steganography, imagePath);
        }
    }

    private static boolean encode(Steganography steganography, String imagePath, String message)
    {
        boolean encodeResult = false;
        try {
            encodeResult = steganography.encode(imagePath, message);
        } catch (IOException e) {
            System.out.println("Error, " + imagePath + " could not be read !");
        }

        if (encodeResult)
            System.out.println("Successfully concealed the message in : " + imagePath);

        return  encodeResult;
    }

    private static void decode(Steganography steganography, String imagePath)
    {
        try
        {
            String message = steganography.decode(imagePath);
            if (message != null && !message.equals(""))
                System.out.println("The retrieved message is : \"" + message + "\".");
        } catch (IOException e) {
            System.out.println("Error, " + imagePath + " could not be read !");
        }
    }

}
