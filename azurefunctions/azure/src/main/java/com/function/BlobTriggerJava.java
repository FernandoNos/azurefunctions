package com.function;

import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.StorageAccount;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.makers.ThumbnailMaker;
import net.coobird.thumbnailator.name.Rename;

import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.BlobTrigger;

/**
 * Azure Functions with Azure Blob trigger.
 */
public class BlobTriggerJava {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     * @throws IOException 
     */
    @FunctionName("BlobTriggerJava")
    @StorageAccount("udemystorageaccountjava_STORAGE")
    public void run(
        @BlobTrigger(name = "content", path = "thumbnail/{name}", dataType = "binary") byte[] content,
        @BindingName("name") String name,
        @BlobOutput(name = "outFile", path = "thumbnail-output/{name}-output.jpg", dataType = "binary") OutputBinding<byte[]> outFile,
        final ExecutionContext context
    ) {
        try{
// Create an input stream from the content
            InputStream inputStream = new ByteArrayInputStream(content);

            // Create a ByteArrayOutputStream to store the resized image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                
            // Resize the image using Thumbnailator
            Thumbnails.of(inputStream)
                    .size(640, 480)
                    .outputFormat("jpg")
                    .toOutputStream(baos);  

            // Get the resized image as a byte array
            byte[] resizedImageBytes = baos.toByteArray();

            // Set the resized image as the output
            outFile.setValue(resizedImageBytes);
        }catch(Exception e){
            context.getLogger().info("error!");
        }
    }
}
