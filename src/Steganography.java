import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Steganography
{
    private int offset = 32;

    public boolean encode(String imagePath, String message) throws IOException
    {
        BufferedImage image = createImageCopyWithUserSpace(getImageBuffer(imagePath));

        addHiddenTextToImage(image, message);
        return (saveImage(image, imagePath));
    }

    private BufferedImage getImageBuffer(String imagePath) throws IOException {
        BufferedImage buffer;

        buffer = ImageIO.read(new File(imagePath));
        return (buffer);
    }

    private BufferedImage createImageCopyWithUserSpace(BufferedImage image)
    {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics2D = copy.createGraphics();

        graphics2D.drawRenderedImage(image, null);
        graphics2D.dispose();
        return (copy);
    }

    private void addHiddenTextToImage(BufferedImage image, String message)
    {
        byte[] imageBytes = getImageBytes(image);
        byte[] messageBytes = message.getBytes();
        byte[] lengthBytes = integerToBytes(messageBytes.length);

        try {
            encodeText(imageBytes, lengthBytes, 0);
            encodeText(imageBytes, messageBytes, this.offset);
        } catch (Exception e) {
            System.out.println("Error, the target can't hold a message");
        }
    }

    private byte[] getImageBytes(BufferedImage image)
    {
        WritableRaster writableRaster = image.getRaster();
        DataBufferByte dataBufferByte = (DataBufferByte)writableRaster.getDataBuffer();

        return (dataBufferByte.getData());
    }

    private byte[] integerToBytes(int value)
    {
        byte byte3 = (byte)((value & 0xFF000000) >>> 24);
        byte byte2 = (byte)((value & 0x00FF0000) >>> 16);
        byte byte1 = (byte)((value & 0x0000FF00) >>> 8 );
        byte byte0 = (byte)((value & 0x000000FF));

        return(new byte[] { byte3,byte2,byte1,byte0 });
    }

    private void encodeText(byte[] image, byte[] textBytes, int offset)
    {
        if (textBytes.length + offset > image.length)
            throw new IllegalArgumentException("File is not long enough to encode the text !");

        for(int add : textBytes) {
            for(int bit=7; bit>=0; --bit, ++offset)
            {
                int b = (add >>> bit) & 1;
                image[offset] = (byte)((image[offset] & 0xFE) | b );
            }
        }
    }

    private boolean saveImage(BufferedImage image, String imagePath)
    {
        String fileName = imagePath.substring(0, imagePath.lastIndexOf('.'));
        String extension = "png";
        File file = new File(fileName + "Steg." + extension);

        try {
            return (ImageIO.write(image, extension, file));
        } catch (Exception e) {
            System.out.println("Error, file could not be saved !");
        }
        return (false);
    }

    public String decode(String imagePath)
    {
        byte[] message;

        try {
            BufferedImage image = createImageCopyWithUserSpace(getImageBuffer(imagePath));

            message = decodeText(getImageBytes(image));
            return (new String(message));
        } catch (Exception e) {
            System.out.println("Error, no hidden message in this image !");
        }
        return ("");
    }

    private byte[] decodeText(byte[] image)
    {
        int length = 0;
        int offset = this.offset;

        for(int index = 0; index < this.offset; ++index)
            length = (length << 1) | (image[index] & 1);

        byte[] result = new byte[length];

        for(int textByte = 0; textByte < result.length; ++textByte )
        {
            for(int textBit = 0; textBit < 8; ++textBit, ++offset)
                result[textByte] = (byte)((result[textByte] << 1) | (image[offset] & 1));
        }
        return (result);
    }
}
